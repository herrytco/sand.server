package systems.nope.worldseed.person;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.world.World;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person add(World world, String name) {
        Optional<Person> referencePerson = personRepository.findByName(name);

        if (referencePerson.isPresent())
            throw new IllegalStateException(String.format("Duplicate Person with name %s", name));

        Person person = new Person(world, name);
        personRepository.save(person);

        return person;
    }
}
