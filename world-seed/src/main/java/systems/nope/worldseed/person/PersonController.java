package systems.nope.worldseed.person;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.person.requests.CreatePersonRequest;
import systems.nope.worldseed.world.World;
import systems.nope.worldseed.world.WorldService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;

    private final WorldService worldService;

    public PersonController(PersonService personService, WorldService worldService) {
        this.personService = personService;
        this.worldService = worldService;
    }

    @PostMapping("/world/{worldId}")
    public ResponseEntity<?> create(
            @PathVariable int worldId,
            @RequestBody CreatePersonRequest createPersonRequest
    ) {
        World world;

        try {
            world = worldService.getWorldRepository().getOne(worldId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with ID '%d' not found.", worldId));
        }

        try {
            Person person = personService.add(world, createPersonRequest.getName());
            return ResponseEntity.ok(person);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Character with name '%s' already exists.", createPersonRequest.getName()));
        }
    }
}
