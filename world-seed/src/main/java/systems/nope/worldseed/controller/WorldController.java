package systems.nope.worldseed.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.UserWorldRoleDto;
import systems.nope.worldseed.exception.DataMissmatchException;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.dto.WorldDto;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.WorldService;
import systems.nope.worldseed.dto.request.NewWorldRequest;
import systems.nope.worldseed.util.WorldDtoFactory;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/worlds")
public class WorldController {

    private final WorldService worldService;
    private final WorldDtoFactory worldDtoFactory;

    public WorldController(WorldService worldService, WorldDtoFactory worldDtoFactory) {
        this.worldService = worldService;
        this.worldDtoFactory = worldDtoFactory;
    }

    @Operation(summary = "Get a set of Worlds identified by their ids.")
    @GetMapping
    public List<WorldDto> multiple(
            @RequestParam(name = "id") Integer[] ids,
            Principal principal
    ) throws DataMissmatchException {
        List<WorldDto> worldDtos = new LinkedList<>();

        for (Integer worldId : ids)
            worldDtos.add(byId(worldId, principal));

        return worldDtos;
    }

    @Operation(summary = "Get a single World by its id.")
    @GetMapping("/id/{id}")
    public WorldDto byId(
            @PathVariable Integer id,
            Principal principal
    ) throws DataMissmatchException {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        World world = worldService.get(id);

        return worldDtoFactory.from(world, user);
    }

    @Operation(summary = "Get a single World by its unique seed.")
    @GetMapping("/seed/{seed}")
    public WorldDto bySeed(
            @PathVariable String seed
    ) {
        Optional<World> seededWorld = worldService.findBySeed(seed);

        if (seededWorld.isEmpty())
            throw new NotFoundException(seed);

        return worldDtoFactory.from(seededWorld.get());
    }

    @Operation(summary = "Add a new world to the system.")
    @PostMapping
    public WorldDto add(
            @RequestBody NewWorldRequest request,
            Authentication authentication
    ) {
        User requester = (User) authentication.getPrincipal();

        UserWorldRoleDto dto = worldService.add(requester, request.getName(), request.description);

        return worldDtoFactory.from(worldService.get(dto.getWorld()));
    }
}
