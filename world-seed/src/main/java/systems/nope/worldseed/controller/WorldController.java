package systems.nope.worldseed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.dto.WorldOwnershipDto;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.dto.WorldDto;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.WorldService;
import systems.nope.worldseed.dto.request.NewWorldRequest;

import java.util.Optional;

@RestController
@RequestMapping("/worlds")
public class WorldController {

    private final WorldService worldService;

    public WorldController(WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping("/id/{id}")
    public WorldDto byId(
            @PathVariable int id
    ) {
        Optional<World> optionalWorld = worldService.find(id);

        if(optionalWorld.isEmpty())
            throw new NotFoundException(id);

        return WorldDto.fromWorld(optionalWorld.get());
    }

    @GetMapping("/seed/{seed}")
    public WorldDto bySeed(
            @PathVariable String seed
    ) {
        Optional<World> seededWorld = worldService.findBySeed(seed);

        if(seededWorld.isEmpty())
            throw new NotFoundException(seed);

        return WorldDto.fromWorld(seededWorld.get());
    }






    @PostMapping
    public ResponseEntity<?> add(
            @RequestBody NewWorldRequest request,
            Authentication authentication
    ) {
        User requester = (User) authentication.getPrincipal();

        try {
            WorldOwnershipDto result = worldService.add(requester, request.getName(), request.description);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
