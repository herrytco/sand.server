package systems.nope.worldseed.person;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.person.requests.CreatePersonRequest;
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
    public ResponseEntity<?> getByApiKey(
            @PathVariable String apiKey
    ) {
        Optional<Person> person = personService.findByApiKey(apiKey);

        return person
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/world/{worldId}")
    public ResponseEntity<?> create(
            @PathVariable int worldId,
            @RequestBody CreatePersonRequest createPersonRequest
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with ID '%d' not found.", worldId));

        World world = optionalWorld.get();

        try {
            Person person = personService.add(world, createPersonRequest.getName());
            return ResponseEntity.ok(person);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Character with name '%s' already exists.", createPersonRequest.getName()));
        }
    }
}
