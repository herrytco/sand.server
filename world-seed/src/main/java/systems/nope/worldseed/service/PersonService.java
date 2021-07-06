package systems.nope.worldseed.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.ImpossibleException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.*;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstance;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstanceConstant;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstanceSynthesized;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.repository.PersonRepository;
import systems.nope.worldseed.repository.stat.StatValueInstanceConstantRepository;
import systems.nope.worldseed.repository.stat.StatValueInstanceSynthesizedRepository;
import systems.nope.worldseed.util.expression.ExpressionUtil;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;
    private final StatSheetService statSheetService;
    private final StatValueInstanceConstantRepository statValueInstanceConstantRepository;
    private final StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository;

    private final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    public PersonService(PersonRepository personRepository, StatSheetService statSheetService, StatValueInstanceConstantRepository statValueInstanceConstantRepository, StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository) {
        this.personRepository = personRepository;
        this.statSheetService = statSheetService;
        this.statValueInstanceConstantRepository = statValueInstanceConstantRepository;
        this.statValueInstanceSynthesizedRepository = statValueInstanceSynthesizedRepository;
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
            StatValuePersonInstance instance = null;

            boolean duplicate = false;

            for (StatValuePersonInstance i : person.getStatValues())
                if (i.getStatValue().getId() == value.getId()) {
                    duplicate = true;
                    break;
                }

            if (duplicate)
                continue;

            if (value instanceof StatValueConstant) {
                instance = new StatValuePersonInstanceConstant(person.getWorld(), value, person, ((StatValueConstant) value).getInitalValue());
                statValueInstanceConstantRepository.save((StatValuePersonInstanceConstant) instance);
            } else if (value instanceof StatValueSynthesized) {
                instance = new StatValuePersonInstanceSynthesized(person.getWorld(), value, person);
                statValueInstanceSynthesizedRepository.save((StatValuePersonInstanceSynthesized) instance);
            }

            if (instance != null)
                person.getStatValues().add(instance);
        }

        person.getStatSheets().add(sheet);
        personRepository.save(person);
    }

    /**
     * Enrich the StatValueInstance. Parses the formula based on the assigned character.
     *
     * @param instanceSynthesized - instance to be enriched
     */
    public void enrichStatInstance(StatValuePersonInstanceSynthesized instanceSynthesized) {
        Person person = instanceSynthesized.getPerson();
        List<SheetNode> sheetForest = constructSheetForest(person.getStatSheets());

        String formula = ((StatValueSynthesized) instanceSynthesized.getStatValue()).getFormula();

        // substitute variables in order of sheet inheritance
        for (SheetNode tree : sheetForest) {
            Optional<Stack<SheetNode>> optionalScopeStack = tree.findStackForStatValueInstance(instanceSynthesized);

            if (optionalScopeStack.isPresent()) {
                Stack<SheetNode> scopeStack = optionalScopeStack.get();

                for (SheetNode scope : scopeStack) {
                    StatSheet sheet = scope.getSheet();

                    // substitute variables in formula with Persons stats
                    for (StatValuePersonInstance stati : person.getStatValues()) {
                        if (stati.getStatValue().getSheet() == sheet) {
                            if (stati instanceof StatValuePersonInstanceConstant)
                                formula = formula.replaceAll(stati.getStatValue().getNameShort(),
                                        ((StatValuePersonInstanceConstant) stati).getValue().toString());
                            else if (stati instanceof StatValuePersonInstanceSynthesized) {
                                StatValuePersonInstanceSynthesized s = ((StatValuePersonInstanceSynthesized) stati);

                                if (s.getValue() != null)
                                    formula = formula.replaceAll(stati.getStatValue().getNameShort(),
                                            s.getValue().toString());
                            }

                        }
                    }
                }
                break;
            }
        }

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

    public void enrichPersonStats(Person person) {
        List<SheetNode> sheetForest = constructSheetForest(person.getStatSheets());

        for (StatValuePersonInstance personInstance : person.getStatValues()) {

            if (personInstance instanceof StatValuePersonInstanceSynthesized) {
                StatValuePersonInstanceSynthesized statInstance = (StatValuePersonInstanceSynthesized) personInstance;
                StatValueSynthesized stat = (StatValueSynthesized) statInstance.getStatValue();

                String formula = stat.getFormula();

                // substitute variables in order of sheet inheritance
                for (SheetNode tree : sheetForest) {
                    Optional<Stack<SheetNode>> optionalScopeStack = tree.findStackForStatValueInstance(personInstance);

                    if (optionalScopeStack.isEmpty())
                        continue;

                    Stack<SheetNode> scopeStack = optionalScopeStack.get();

                    for (SheetNode scope : scopeStack) {
                        StatSheet sheet = scope.getSheet();

                        // substitute variables in formula with Persons stats
                        for (StatValuePersonInstance stati : person.getStatValues()) {
                            if (stati.getStatValue().getSheet() == sheet) {
                                if (stati instanceof StatValuePersonInstanceConstant)
                                    formula = formula.replaceAll(
                                            " " + stati.getStatValue().getNameShort() + " ",
                                            " " + stati.getValue().toString() + " "
                                    );
                                else if (stati instanceof StatValuePersonInstanceSynthesized) {
                                    StatValuePersonInstanceSynthesized s = ((StatValuePersonInstanceSynthesized) stati);

                                    if (s.getValue() != null)
                                        formula = formula.replaceAll(
                                                " " + stati.getStatValue().getNameShort() + " ",
                                                " " + s.getValue().toString() + " "
                                        );
                                }

                            }
                        }
                    }
                    break;

                }

                try {
                    double result = ExpressionUtil.parseExpression(formula);
                    statInstance.setValue((int) result);
                } catch (IllegalArgumentException e) {
                    statInstance.setValue(-1);
                    logger.error(e.getMessage());
                } catch (IllegalStateException e) {
                    statInstance.setValue(-1);
                    logger.error(String.format("Internal Error while parsing formula: '%s'", formula));
                }
            }

        }
    }

    public void addItemToPerson(Person person, Item item) {
        final Set<StatSheet> requiredStatSheets = new HashSet<>();

        item.getActions().forEach((action -> {
            requiredStatSheets.addAll(action.getRequiredStatSheets());
        }));

        Set<StatSheet> groundedRequiredStatSheets = statSheetService.groundStatSheets(requiredStatSheets);
        Set<StatSheet> providedStatSheets = new HashSet<>(item.getStatSheets());
        providedStatSheets.addAll(person.getStatSheets());
        providedStatSheets = statSheetService.groundStatSheets(providedStatSheets);

        if (!providedStatSheets.containsAll(groundedRequiredStatSheets))
            throw new ImpossibleException();

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
