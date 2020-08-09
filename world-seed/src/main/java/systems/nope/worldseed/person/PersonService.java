package systems.nope.worldseed.person;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.world.World;

import java.util.Optional;
import java.util.Random;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
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

        while(optionalPerson.isPresent()) {
            key = createRandomString(256);
            optionalPerson = personRepository.findByApiKey(key);
        }

        return key;
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
