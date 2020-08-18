package systems.nope.worldseed.world;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.user.User;
import systems.nope.worldseed.user.WorldOwnership;
import systems.nope.worldseed.util.exceptions.NotFoundException;
import systems.nope.worldseed.world.requests.NewWorldRequest;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/worlds")
public class WorldController {

    private final WorldService worldService;

    public WorldController(WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping("/id/{id}")
    public OutWorld byId(
            @PathVariable int id
    ) {
        Optional<World> optionalWorld = worldService.find(id);

        if(optionalWorld.isEmpty())
            throw new NotFoundException(id);

        return OutWorld.fromWorld(optionalWorld.get());
    }

    @GetMapping("/seed/{seed}")
    public OutWorld bySeed(
            @PathVariable String seed
    ) {
        Optional<World> seededWorld = worldService.findBySeed(seed);

        if(seededWorld.isEmpty())
            throw new NotFoundException(seed);

        return OutWorld.fromWorld(seededWorld.get());
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
