package systems.nope.worldseed.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.UserWorldRoleDto;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.dto.WorldDto;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.WorldService;
import systems.nope.worldseed.dto.request.NewWorldRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/worlds")
public class WorldController {

    private final WorldService worldService;

    public WorldController(WorldService worldService) {
        this.worldService = worldService;
    }

    @Operation(summary = "Get a set of Worlds identified by their ids.")
    @GetMapping
    public List<WorldDto> multiple(
            @RequestParam(name = "id") Integer[] ids
    ) {
        return Stream.of(ids).map(this::byId).collect(Collectors.toList());
    }

    @Operation(summary = "Get a single World by its id.")
    @GetMapping("/id/{id}")
    public WorldDto byId(
            @PathVariable Integer id
    ) {
        Optional<World> optionalWorld = worldService.find(id);

        if (optionalWorld.isEmpty())
            throw new NotFoundException(id);

        return WorldDto.fromWorld(optionalWorld.get());
    }

    @Operation(summary = "Get a single World by its unique seed.")
    @GetMapping("/seed/{seed}")
    public WorldDto bySeed(
            @PathVariable String seed
    ) {
        Optional<World> seededWorld = worldService.findBySeed(seed);

        if (seededWorld.isEmpty())
            throw new NotFoundException(seed);

        return WorldDto.fromWorld(seededWorld.get());
    }

    @Operation(summary = "Add a new world to the system.")
    @PostMapping
    public UserWorldRoleDto add(
            @RequestBody NewWorldRequest request,
            Authentication authentication
    ) {
        User requester = (User) authentication.getPrincipal();

        return worldService.add(requester, request.getName(), request.description);
    }
}
