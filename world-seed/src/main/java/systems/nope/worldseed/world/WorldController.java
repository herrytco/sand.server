package systems.nope.worldseed.world;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.world.requests.NewWorldRequest;

import java.util.List;

@RestController
@RequestMapping("/worlds")
public class WorldController {

    private final WorldService worldService;

    public WorldController(WorldService worldService) {
        this.worldService = worldService;
    }

    @PostMapping
    public ResponseEntity<?> add(
            @RequestBody NewWorldRequest request
    ) {
        if (worldService.add(request.name, request.description)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
