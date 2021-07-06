package systems.nope.worldseed.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.person.PersonService;
import systems.nope.worldseed.world.WorldTestUtil;

import java.util.Optional;

@Service
public class PersonTestUtil {

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private PersonService personService;

    private Person ensuredPerson;

    public Person getEnsuredPerson() {
        return ensuredPerson;
    }

    public Person ensureTestPersonExists() {
        World testWorld = worldTestUtil.ensureTestWorldExists();
        return ensurePersonExists(testWorld, PersonConstants.personName);
    }

    public Person ensurePersonExists(World world, String name) {
        Optional<Person> optionalPerson = personService.findByWorldAndName(world, name);

        if(optionalPerson.isPresent()) {
            ensuredPerson = optionalPerson.get();
            return ensuredPerson;
        }

        ensuredPerson = personService.add(world, name);
        return ensuredPerson;
    }
}
