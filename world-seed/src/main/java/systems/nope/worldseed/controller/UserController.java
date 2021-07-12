package systems.nope.worldseed.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.UserDto;
import systems.nope.worldseed.dto.UserWorldRoleDto;
import systems.nope.worldseed.model.*;
import systems.nope.worldseed.repository.UserWorldRoleRepository;
import systems.nope.worldseed.service.RoleService;
import systems.nope.worldseed.service.UserService;
import systems.nope.worldseed.dto.request.RegistrationRequest;
import systems.nope.worldseed.service.WorldService;
import systems.nope.worldseed.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final WorldService worldService;
    private final RoleService roleService;
    private final UserWorldRoleRepository userWorldRoleRepository;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, WorldService worldService, RoleService roleService, UserWorldRoleRepository userWorldRoleRepository) {
        this.userService = userService;
        this.worldService = worldService;
        this.roleService = roleService;
        this.userWorldRoleRepository = userWorldRoleRepository;
    }

    @Operation(summary = "Add a new User to the system.")
    @PostMapping
    public void add(
            @RequestBody RegistrationRequest request
    ) {
        logger.info(String.format("UserAddRequest %s", request));

        userService.addUser(request.getName(), request.getEmail(), request.getPassword());
    }

    @Operation(summary = "Get a single User by its ID.")
    @GetMapping("/id/{id}")
    public UserDto one(
            @PathVariable int id
    ) {
        logger.info(String.format("UserController.one(id:%d)", id));

        return UserDto.fromUser(userService.getUserRepository().getOne(id));
    }

    @Operation(summary = "Get all Users for a World.")
    @GetMapping("/id/{id}/worlds")
    public List<UserWorldRoleDto> worlds(@PathVariable int id) {
        logger.info(String.format("UserController.worlds(id:%d)", id));

        Optional<User> optionalRequester = userService.findById(id);

        if (optionalRequester.isEmpty())
            throw new NotFoundException(id);

        User requester = optionalRequester.get();

        return requester.getWorldRoles().stream().map(UserWorldRoleDto::fromUserWorldRole).collect(Collectors.toList());
    }

    @Operation(summary = "Add a User to a World with the Role 'Visitor'.")
    @PostMapping("/id/{id}/worlds/{worldId}")
    public void joinWorld(
            @PathVariable int id,
            @PathVariable int worldId
    ) {
        logger.info(String.format("UserController.joinWorld(userId:%d, worldId:%d)", id, worldId));

        Optional<User> optionalRequester = userService.findById(id);
        if (optionalRequester.isEmpty())
            throw new NotFoundException(id);

        User requester = optionalRequester.get();

        worldService.join(
                requester,
                worldService.get(worldId)
        );
    }
}
