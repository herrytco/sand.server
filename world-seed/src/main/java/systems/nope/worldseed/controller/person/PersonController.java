package systems.nope.worldseed.controller.person;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.PersonDto;
import systems.nope.worldseed.dto.request.AddNamedResourceRequest;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.service.PersonService;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.WorldService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;
    private final WorldService worldService;
    private final ItemService itemService;

    public PersonController(PersonService personService, WorldService worldService, ItemService itemService) {
        this.personService = personService;
        this.worldService = worldService;
        this.itemService = itemService;
    }

    @PostMapping("/id/{personId}/items/{itemId}")
    public void giveItemToPerson(
            @PathVariable Integer itemId,
            @PathVariable Integer personId
    ) {
        personService.addItemToPerson(
                personService.get(personId, false),
                itemService.get(itemId)
        );
    }

    @Operation(summary = "Get a set of Characters which belong to the given world.")
    @GetMapping("/world/id/{worldId}")
    public List<PersonDto> getByWorld(
            @PathVariable Integer worldId
    ) {
        return personService.findByWorld(worldService.get(worldId))
                .stream().map(PersonDto::fromPerson)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a set of Characters identified by their ids.")
    @GetMapping
    public List<PersonDto> getMultiple(
            @RequestParam(name = "id") Integer[] ids
    ) {
        return Stream.of(ids).map(this::getById).collect(Collectors.toList());
    }

    @Operation(summary = "Get a single Character by its id.")
    @GetMapping("/id/{id}")
    public PersonDto getById(
            @PathVariable Integer id
    ) {
        Optional<Person> person = personService.findById(id);

        if (person.isEmpty())
            throw new NotFoundException(id);

        return PersonDto.fromPerson(person.get());
    }

    @Operation(summary = "Get a single Character by its unique API key.")
    @GetMapping("/api/{apiKey}")
    public PersonDto getByApiKey(
            @PathVariable String apiKey
    ) {
        Optional<Person> person = personService.findByApiKey(apiKey);

        if (person.isEmpty())
            throw new NotFoundException(apiKey);

        return PersonDto.fromPerson(person.get());
    }

    @Operation(summary = "Create a new character in a given world.")
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
