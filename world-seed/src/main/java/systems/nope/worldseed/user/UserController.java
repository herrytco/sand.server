package systems.nope.worldseed.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.role.Role;
import systems.nope.worldseed.role.RoleService;
import systems.nope.worldseed.role.RoleType;
import systems.nope.worldseed.user.requests.RegistrationRequest;
import systems.nope.worldseed.world.UserWorldRole;
import systems.nope.worldseed.world.UserWorldRoleRepository;
import systems.nope.worldseed.world.World;
import systems.nope.worldseed.world.WorldService;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final WorldService worldService;
    private final RoleService roleService;
    private final UserWorldRoleRepository userWorldRoleRepository;

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
        return userService.getUserRepository().getOne(id);
    }

    @GetMapping("/id/{id}/worlds")
    public ResponseEntity<?> worlds(@PathVariable int id) {
        try {
            User requester = userService.getUserRepository().getOne(id);
            return ResponseEntity.ok(WorldOwnership.fromUser(requester));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User ID '%d' not found.", id));
        }
    }

    @PostMapping("/id/{id}/worlds/{worldId}")
    public ResponseEntity<?> joinWorld(
            @PathVariable int id,
            @PathVariable int worldId
    ) {
        User requester;

        try {
            requester = userService.getUserRepository().getOne(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User ID '%d' not found.", id));
        }

        World worldToJoin;

        try {
            worldToJoin = worldService.getWorldRepository().getOne(worldId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User ID '%d' not found.", worldId));
        }

        Role visitor = roleService.getRoleForType(RoleType.Visitor);

        UserWorldRole userWorldRole = new UserWorldRole(requester, worldToJoin, visitor);
        userWorldRoleRepository.save(userWorldRole);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> add(
            @RequestBody RegistrationRequest request
    ) {

        System.out.println(String.format("UserAddRequest %s", request));
        if (userService.addUser(request.name, request.email, request.password)) {
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
