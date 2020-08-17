package systems.nope.worldseed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.role.Role;
import systems.nope.worldseed.role.RoleService;
import systems.nope.worldseed.role.RoleType;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.user.User;
import systems.nope.worldseed.user.UserService;
import systems.nope.worldseed.world.UserWorldRole;
import systems.nope.worldseed.world.World;
import systems.nope.worldseed.world.WorldService;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class DataGeneration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorldService worldService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    @Autowired
    private UserTestUtil userTestUtil;

    /**
     * used to initialize debug-data on the server
     */
    @Test
    public void insertData() throws Exception {
        userTestUtil.ensureUserExists(DebugConstants.debugName, DebugConstants.debugUsername, DebugConstants.debugPassword);
        Optional<String> tokenDebug = userTestUtil.authenticateUser(DebugConstants.debugUsername, DebugConstants.debugPassword);

        assert tokenDebug.isPresent();
        String token = tokenDebug.get();

        Optional<User> optionalUser = userService.getUserRepository().findByEmail(DebugConstants.debugUsername);
        assert optionalUser.isPresent();

        User user = optionalUser.get();

        String seed = "123456";

        Optional<World> optionalWorld = worldService.getWorldRepository().findBySeed(seed);
        World world;

        if (optionalWorld.isEmpty()) {
            world = new World("Herrys World", "A World beyond my ultimate rulership!", seed);
            worldService.getWorldRepository().save(world);
        } else
            world = optionalWorld.get();

        Role owner = roleService.getRoleForType(RoleType.Owner);

        UserWorldRole userWorldRole = new UserWorldRole(user, world, owner);
        user.getWorldRoles().add(userWorldRole);
    }
}
