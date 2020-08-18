package systems.nope.worldseed.person;

import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.person.requests.CreatePersonRequest;
import systems.nope.worldseed.util.exceptions.NotFoundException;
import systems.nope.worldseed.world.World;
import systems.nope.worldseed.world.WorldService;

import java.util.Optional;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;
    private final WorldService worldService;

    public PersonController(PersonService personService, WorldService worldService) {
        this.personService = personService;
        this.worldService = worldService;
    }

    @GetMapping("/api/{apiKey}")
    public OutPerson getByApiKey(
            @PathVariable String apiKey
    ) {
        Optional<Person> person = personService.findByApiKey(apiKey);

        if (person.isEmpty())
            throw new NotFoundException(apiKey);

        return OutPerson.fromPerson(person.get());
    }

    @GetMapping("/id/{id}")
    public OutPerson getById(
            @PathVariable Integer id
    ) {
        Optional<Person> person = personService.findById(id);

        if (person.isEmpty())
            throw new NotFoundException(id);

        return OutPerson.fromPerson(person.get());
    }


    @PostMapping("/world/{worldId}")
    public OutPerson create(
            @PathVariable int worldId,
            @RequestBody CreatePersonRequest createPersonRequest
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            throw new NotFoundException(worldId);

        World world = optionalWorld.get();

        Person person = personService.add(world, createPersonRequest.getName());

        return OutPerson.fromPerson(person);
    }
}
