package systems.nope.worldseed.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.stat.StatValueInstanceConstantRepository;
import systems.nope.worldseed.stat.StatValueInstanceSynthesizedRepository;
import systems.nope.worldseed.stat.model.StatValueInstance;
import systems.nope.worldseed.stat.model.StatValueInstanceConstant;
import systems.nope.worldseed.stat.model.StatValueInstanceSynthesized;
import systems.nope.worldseed.stat.sheet.*;
import systems.nope.worldseed.world.World;

import java.util.*;

@Service
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

    private String findApiKey() {
        String key = createRandomString(256);

        Optional<Person> optionalPerson = personRepository.findByApiKey(key);

        while (optionalPerson.isPresent()) {
            key = createRandomString(256);
            optionalPerson = personRepository.findByApiKey(key);
        }

        return key;
    }

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

            if (value instanceof StatValueConstant) {
                instance = new StatValueInstanceConstant(person.getWorld(), value, person, ((StatValueConstant) value).getInitalValue());
                statValueInstanceConstantRepository.save((StatValueInstanceConstant) instance);
            } else if (value instanceof StatValueSynthesized) {
                instance = new StatValueInstanceSynthesized(person.getWorld(), value, person);
                statValueInstanceSynthesizedRepository.save((StatValueInstanceSynthesized) instance);
            }

            if (instance != null)
                person.getStatValues().add(instance);
        }

        person.getStatSheets().add(sheet);
        personRepository.save(person);
    }

    public Optional<Person> findByApiKey(String apiKey) {
        Optional<Person> optionalPerson = personRepository.findByApiKey(apiKey);

        if (optionalPerson.isEmpty())
            return optionalPerson;

        Person person = optionalPerson.get();

        enrichPersonStats(person);

        return Optional.of(person);
    }

    public void enrichPersonStats(Person person) {
        statSheetService.enrichPersonStats(person);
    }

    public Person add(World world, String name) {
        Optional<Person> referencePerson = personRepository.findByName(name);

        if (referencePerson.isPresent())
            throw new IllegalStateException(String.format("Duplicate Person with name %s", name));

        Person person = new Person(world, name, findApiKey());
        personRepository.save(person);

        return person;
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }
}
