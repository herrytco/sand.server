package systems.nope.worldseed.service.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.*;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.model.person.PersonNote;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceSynthesized;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.repository.person.PersonNoteRepository;
import systems.nope.worldseed.repository.person.PersonRepository;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.service.StatValueInstanceService;
import systems.nope.worldseed.util.StatContext;
import systems.nope.worldseed.util.StatUtils;
import systems.nope.worldseed.util.Symbol;
import systems.nope.worldseed.util.data.DataStructure;
import systems.nope.worldseed.util.expression.ExpressionUtil;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonNoteRepository personNoteRepository;
    private final StatUtils statUtils;
    private final StatValueInstanceService statValueInstanceService;

    private final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    public PersonService(PersonRepository personRepository, PersonNoteRepository personNoteRepository,
                         StatUtils statUtils, StatValueInstanceService statValueInstanceService) {
        this.personRepository = personRepository;
        this.personNoteRepository = personNoteRepository;
        this.statUtils = statUtils;
        this.statValueInstanceService = statValueInstanceService;
    }

    public Optional<Person> find(int id) {
        return personRepository.findById(id);
    }

    public Person get(int id) {
        return get(id, true);
    }

    public Person get(int id, boolean enrich) {
        Optional<Person> optionalPerson = find(id);

        if (optionalPerson.isEmpty())
            throw new NotFoundException(id);

        Person person = optionalPerson.get();

        if (enrich)
            enrichPersonStats(person);

        return person;
    }

    public List<Person> findByWorld(World world) {
        return personRepository.findByWorld(world);
    }

    public Optional<Person> findByWorldAndName(World world, String name) {
        Optional<Person> optionalPerson = personRepository.findByWorldAndName(world, name);

        if (optionalPerson.isEmpty())
            return optionalPerson;

        Person person = optionalPerson.get();

        enrichPersonStats(person);

        return Optional.of(person);
    }

    public Optional<Person> findByApiKey(String apiKey) {
        Optional<Person> optionalPerson = personRepository.findByApiKey(apiKey);

        if (optionalPerson.isEmpty())
            return optionalPerson;

        Person person = optionalPerson.get();

        enrichPersonStats(person);

        return Optional.of(person);
    }

    public Optional<Person> findById(Integer id) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isEmpty())
            return optionalPerson;

        Person person = optionalPerson.get();

        enrichPersonStats(person);

        return Optional.of(person);
    }

    public void addNoteToPerson(Person person, String content) {
        PersonNote noteNew = new PersonNote();
        noteNew.setContent(content);
        noteNew.setPerson(person);
        personNoteRepository.save(noteNew);
        person.getNotes().add(noteNew);
    }

    public void updateControllingUserOfPerson(Person person, User user) {
        person.setControllingUser(user);
        personRepository.save(person);
    }

    /**
     * Adds the stat sheet including its parents to the person. All StatValues get added as well.
     *
     * @param person - person the stat sheet gets assigned to
     * @param sheet  - target stat sheet
     */
    public void addStatSheetToPerson(Person person, StatSheet sheet) {
        if (sheet.getParent() != null)
            addStatSheetToPerson(person, sheet.getParent());

        if (person.getStatSheets().contains(sheet)) {
            person.getStatSheets().remove(sheet);
            personRepository.save(person);
        }

        for (StatValue value : sheet.getStatValues()) {
            StatValueInstance instance = null;

            boolean duplicate = false;

            for (StatValueInstance i : person.getStatValues())
                if (i.getStatValue().getId() == value.getId()) {
                    duplicate = true;
                    break;
                }

            if (duplicate)
                continue;

            if (value instanceof StatValueConstant)
                instance = statValueInstanceService.add(
                        person.getWorld(),
                        value,
                        ((StatValueConstant) value).getInitalValue()
                );
            else if (value instanceof StatValueSynthesized) {
                instance = statValueInstanceService.add(
                        person.getWorld(),
                        value
                );
            }

            if (instance != null)
                person.getStatValues().add(instance);
        }

        person.getStatSheets().add(sheet);
        personRepository.save(person);

        realizePersonResources(person);
    }

    public void realizePersonResources(Person person) {
        enrichPersonStats(person);

        for (StatValueInstance instance : person.getStatValues()) {
            if (instance instanceof StatValueInstanceSynthesized && instance.getStatValue().getResource()
                    && instance.getResource() != null && instance.getResource().getValue() == null) {

                instance.getResource().setValue(instance.getValue());
            }
        }

        personRepository.save(person);
    }

    public void enrichPersonStats(Person person) {
        if (person.getStatValues().size() == 0)
            return;

        List<DataStructure<StatSheet>> forest = statUtils.constructSheetForest(person.getStatSheets());

        for (DataStructure<StatSheet> tree : forest) {
            StatContext context = new StatContext();

            for (StatSheet node : tree) {
                for (StatValueInstance instance : person.getStatValues()) {
                    // ignore if the instance is not in the current sheet
                    if (instance.getStatValue().getSheet().getId() != node.getId())
                        continue;

                    // if the StatValue needs to be enriched
                    if (instance instanceof StatValueInstanceSynthesized && instance.getValue() == null)
                        enrichStatInstance((StatValueInstanceSynthesized) instance, context);

                    context.addSymbol(new Symbol(instance.getStatValue().getNameShort(), instance.getValue()));
                }
            }
        }

    }

    /**
     * Enrich the StatValueInstance. Parses the formula based on the assigned character.
     *
     * @param instanceSynthesized - instance to be enriched
     */
    public void enrichStatInstance(StatValueInstanceSynthesized instanceSynthesized, StatContext context) {
        String formula = ((StatValueSynthesized) instanceSynthesized.getStatValue()).getFormula();

        formula = statUtils.enrichExpression(formula, context.getSymbols());

        try {
            double result = ExpressionUtil.parseExpression(formula);
            instanceSynthesized.setValue((int) result);
        } catch (IllegalArgumentException e) {
            instanceSynthesized.setValue(-1);
            logger.error(e.getMessage());
        } catch (IllegalStateException e) {
            instanceSynthesized.setValue(-1);
            logger.error(String.format("Internal Error while parsing formula: '%s'", formula));
        }
    }

    public void addItemToPerson(Person person, Item item) {
        person.getItems().add(item);
        personRepository.save(person);
    }

    /**
     * @param statSheets - sheets the tree should constructed of
     * @return - forest of dependencies of all given sheets
     */
    public List<SheetNode> constructSheetForest(List<StatSheet> statSheets) {
        List<StatSheet> statSheetsWorkingSet = new LinkedList<>(statSheets);
        List<SheetNode> forest = new LinkedList<>();
        List<StatSheet> used = new LinkedList<>();

        boolean changed;

        do {
            changed = false;

            for (StatSheet sheet : statSheetsWorkingSet) {
                // root node, add new tree to forest
                if (sheet.getParent() == null) {
                    forest.add(new SheetNode(sheet));
                    used.add(sheet);
                    changed = true;
                }

                // child node, search for tree, the sheet belongs to
                for (SheetNode tree : forest) {
                    Optional<SheetNode> parentNode = tree.findSheetNodeContainingSheet(sheet.getParent());

                    if (parentNode.isPresent()) {
                        parentNode.get().addChild(sheet);
                        used.add(sheet);
                        changed = true;
                    }
                }
            }

            statSheetsWorkingSet.removeAll(used);
        } while (changed);

        return forest;
    }

    public Person add(World world, String name) {
        Optional<Person> referencePerson = personRepository.findByWorldAndName(world, name);

        if (referencePerson.isPresent())
            throw new IllegalStateException(String.format("Duplicate Person with name %s", name));

        Person person = new Person(world, name, findApiKey());
        personRepository.save(person);

        return person;
    }

    private String findApiKey() {
        String key = createRandomString(256);

        Optional<Person> optionalPerson = personRepository.findByApiKey(key);

        while (optionalPerson.isPresent()) {
            key = createRandomString(256);
            optionalPerson = personRepository.findByApiKey(key);
        }

        return key;
    }

    public String createRandomString(int length) {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }
}
