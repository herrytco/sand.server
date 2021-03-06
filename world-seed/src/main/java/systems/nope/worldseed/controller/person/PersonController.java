package systems.nope.worldseed.controller.person;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import systems.nope.worldseed.dto.ItemDto;
import systems.nope.worldseed.dto.person.PersonDto;
import systems.nope.worldseed.dto.person.PersonNoteDto;
import systems.nope.worldseed.dto.request.AddNamedResourceRequest;
import systems.nope.worldseed.dto.request.UpdateControllingUserRequest;
import systems.nope.worldseed.exception.DataMissmatchException;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.service.UserService;
import systems.nope.worldseed.service.person.PersonService;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.WorldService;
import systems.nope.worldseed.util.file.PortraitFileUtil;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
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
    private final UserService userService;
    private final PortraitFileUtil portraitFileUtil;

    public PersonController(PersonService personService, WorldService worldService, ItemService itemService,
                            UserService userService, PortraitFileUtil portraitFileUtil) {
        this.personService = personService;
        this.worldService = worldService;
        this.itemService = itemService;
        this.userService = userService;
        this.portraitFileUtil = portraitFileUtil;
    }

    @Transactional
    @Operation(summary = "Get a set of Characters which belong to the given world.")
    @GetMapping("/world/id/{worldId}")
    public List<PersonDto> getByWorld(
            @PathVariable Integer worldId,
            Principal principal
    ) throws DataMissmatchException {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return personService.findByWorldAndUser(worldService.get(worldId), user)
                .stream().map(PersonDto::fromPerson)
                .collect(Collectors.toList());
    }

    ////
    // ITEMS
    ////
    @GetMapping("/{personId}/items")
    public List<ItemDto> itemsForPerson(
            @PathVariable Integer personId
    ) {
        return personService.get(personId).getItems().stream().map(ItemDto::fromItem).collect(Collectors.toList());
    }

    @PostMapping("/{personId}/items/{itemId}")
    public void giveItemToPerson(
            @PathVariable Integer itemId,
            @PathVariable Integer personId
    ) {
        personService.addItemToPerson(
                personService.get(personId, false),
                itemService.get(itemId)
        );
    }

    ////
    // NOTES
    ////
    @GetMapping("/{personId}/notes")
    public List<PersonNoteDto> notesForPerson(
            @PathVariable Integer personId
    ) {
        return personService.get(personId).getNotes().stream().map(PersonNoteDto::fromNote).collect(Collectors.toList());
    }

    @PostMapping("/{personId}/notes")
    public PersonNoteDto addNoteToPerson(
            @PathVariable Integer personId,
            @RequestBody PersonNoteDto note
    ) {
        return PersonNoteDto.fromNote(
                personService.addNoteToPerson(
                        personService.get(personId),
                        note.getContent()
                )
        );
    }

    @PostMapping("/{personId}/portrait")
    public void uploadPortrait(
            @PathVariable Integer personId,
            MultipartFile file
    ) {
        try {
            if (file == null || file.getBytes().length == 0)
                throw new NotFoundException("'file' required");

            personService.setPortraitOfPerson(
                    personService.get(personId),
                    file.getBytes()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PutMapping("/{personId}/controllingUser")
    public void updateControllingUserForPerson(
            @PathVariable Integer personId,
            @RequestBody UpdateControllingUserRequest request
    ) {
        personService.updateControllingUserOfPerson(
                personService.get(personId),
                userService.getUserRepository().getOne(request.getUserId())
        );
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
