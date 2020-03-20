package systems.nope.worldseed.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.Authenticator;
import systems.nope.worldseed.world.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserWorldTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Authenticator authenticator;
    @Autowired
    private WorldService worldService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserWorldRoleRepository userWorldRoleRepository;

    @BeforeEach
    public void ensureUserExists() {
        authenticator.ensureTestuserExists();
    }

    @BeforeEach
    public void ensureWorldExists() {
        Optional<World> worldPrime = worldService.getWorldRepository().findBySeed(WorldConstants.nonExistingWorldSeed);

        if (worldPrime.isEmpty()) {
            User testUser = authenticator.ensureTestuserExists();

            worldService.add(testUser, WorldConstants.nonExistingWorldName,
                    WorldConstants.worldDescription,
                    WorldConstants.nonExistingWorldSeed
            );
        }
    }

    @Test
    public void getWorld() throws Exception {
        Optional<World> optionalWorld = worldService.getWorldRepository().findBySeed(WorldConstants.nonExistingWorldSeed);
        assert optionalWorld.isPresent();

        World testWorld = optionalWorld.get();

        Optional<User> optionalUser = userService.getUserRepository().findByEmail(UserConstants.nonExistingEmail);
        assert optionalUser.isPresent();

        User testUser = optionalUser.get();

        String token = authenticator.authenticateTestUser();

        mockMvc.perform(
                get(String.format("%s/id/%d", WorldConstants.endpoint, testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());

        userWorldRoleRepository.deleteAllByUser(testUser);
    }

    /**
     * lets the testuser join the testworld and deletes the binding afterwards
     *
     * @throws Exception
     */
    @Test
    public void joinWorld() throws Exception {
        Optional<World> optionalWorld = worldService.getWorldRepository().findBySeed(WorldConstants.nonExistingWorldSeed);
        assert optionalWorld.isPresent();

        World testWorld = optionalWorld.get();

        Optional<User> optionalUser = userService.getUserRepository().findByEmail(UserConstants.nonExistingEmail);
        assert optionalUser.isPresent();

        User testUser = optionalUser.get();

        String token = authenticator.authenticateTestUser();

        mockMvc.perform(
                post(String.format("%s/id/%d/worlds/%d", UserConstants.endpoint, testUser.getId(), testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());

        List<UserWorldRole> rolesForTestUser = userWorldRoleRepository.findAllByUser(testUser);
        assert rolesForTestUser.size() > 0;

        userWorldRoleRepository.deleteAllByUser(testUser);
    }
}
