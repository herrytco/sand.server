package systems.nope.worldseed.world;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.user.User;
import systems.nope.worldseed.user.WorldOwnership;
import systems.nope.worldseed.world.requests.NewWorldRequest;

import java.util.Optional;

@RestController
@RequestMapping("/worlds")
public class WorldController {

    private final WorldService worldService;

    public WorldController(WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping("/seed/{seed}")
    public ResponseEntity<?> bySeed(
            @PathVariable String seed
    ) {
        Optional<World> seededWorld = worldService.getWorldRepository().findBySeed(seed);

        if (seededWorld.isPresent())
            return ResponseEntity.ok(seededWorld);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with seed '%s' not found.", seed));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> bySeed(
            @PathVariable int id
    ) {
        Optional<World> seededWorld = worldService.getWorldRepository().findById(id);

        if (seededWorld.isPresent())
            return ResponseEntity.ok(seededWorld);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with seed '%d' not found.", id));
    }

    @PostMapping
    public ResponseEntity<?> add(
            @RequestBody NewWorldRequest request,
            Authentication authentication
    ) {
        User requester = (User) authentication.getPrincipal();

        try {
            WorldOwnership result = worldService.add(requester, request.name, request.description);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
