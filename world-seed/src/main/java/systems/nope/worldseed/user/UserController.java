package systems.nope.worldseed.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.role.Role;
import systems.nope.worldseed.role.RoleService;
import systems.nope.worldseed.role.RoleType;
import systems.nope.worldseed.user.requests.RegistrationRequest;
import systems.nope.worldseed.util.exceptions.NotFoundException;
import systems.nope.worldseed.world.*;

import java.util.List;
import java.util.Optional;

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

    @RequestMapping("/id/{id}")
    public User one(
            @PathVariable int id
    ) {
        logger.info(String.format("UserController.one(id:%d)", id));

        return userService.getUserRepository().getOne(id);
    }

    @GetMapping("/id/{id}/worlds")
    public List<WorldOwnership> worlds(@PathVariable int id) {
        logger.info(String.format("UserController.worlds(id:%d)", id));

        Optional<User> optionalRequester = userService.findById(id);

        if (optionalRequester.isEmpty())
            throw new NotFoundException(id);

        User requester = optionalRequester.get();

        return WorldOwnership.fromUser(requester);
    }

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


        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            throw new NotFoundException(worldId);

        World worldToJoin = optionalWorld.get();

        Role visitor = roleService.getRoleForType(RoleType.Visitor);

        UserWorldRole userWorldRole = new UserWorldRole(requester, worldToJoin, visitor);
        userWorldRoleRepository.save(userWorldRole);
    }

    @PostMapping
    public void add(
            @RequestBody RegistrationRequest request
    ) {
        logger.info(String.format("UserAddRequest %s", request));

        userService.addUser(request.name, request.email, request.password);
    }
}
