package systems.nope.worldseed.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.*;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceConstant;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceSynthesized;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.repository.PersonRepository;
import systems.nope.worldseed.repository.stat.StatValueInstanceConstantRepository;
import systems.nope.worldseed.repository.stat.StatValueInstanceSynthesizedRepository;

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

    public void enrichPersonStats(Person person) {
        statSheetService.enrichPersonStats(person);
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
