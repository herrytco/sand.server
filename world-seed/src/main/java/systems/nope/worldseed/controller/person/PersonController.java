package systems.nope.worldseed.controller.person;

import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.PersonDto;
import systems.nope.worldseed.dto.request.AddNamedResourceRequest;
import systems.nope.worldseed.dto.request.MultiIdRequest;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.service.PersonService;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.WorldService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public PersonDto getByApiKey(
            @PathVariable String apiKey
    ) {
        Optional<Person> person = personService.findByApiKey(apiKey);

        if (person.isEmpty())
            throw new NotFoundException(apiKey);

        return PersonDto.fromPerson(person.get());
    }

    @GetMapping("/id/{id}")
    public PersonDto getById(
            @PathVariable Integer id
    ) {
        Optional<Person> person = personService.findById(id);

        if (person.isEmpty())
            throw new NotFoundException(id);

        return PersonDto.fromPerson(person.get());
    }

    @GetMapping
    List<PersonDto> getMultiple(
            @RequestBody MultiIdRequest request
    ) {
        return request.getIds().stream().map(this::getById).collect(Collectors.toList());
    }

    @PostMapping("/world/{worldId}")
    public PersonDto create(
            @PathVariable int worldId,
            @RequestBody AddNamedResourceRequest createPersonRequest
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            throw new NotFoundException(worldId);

        World world = optionalWorld.get();

        Person person = personService.add(world, createPersonRequest.getName());

        return PersonDto.fromPerson(person);
    }
}
