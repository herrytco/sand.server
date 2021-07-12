package systems.nope.worldseed.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import systems.nope.worldseed.TestConstants;
import systems.nope.worldseed.dto.WorldDto;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.model.UserWorldRole;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.repository.UserWorldRoleRepository;
import systems.nope.worldseed.service.UserService;
import systems.nope.worldseed.service.WorldService;
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
    private UserTestUtil userTestUtil;
    @Autowired
    private WorldService worldService;

    @Autowired
    private UserWorldRoleRepository userWorldRoleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void ensureDataExists() {
        userTestUtil.ensureTestuserExists();

        // ensure non existing world does not exist
        Optional<World> optionalNonExistingWorld = worldService.getWorldRepository().findBySeed(WorldConstants.nonExistingWorldSeed);
        optionalNonExistingWorld.ifPresent(world -> worldService.getWorldRepository().delete(world));

        worldTestUtil.ensureWorldExists(WorldConstants.existingWorldName, WorldConstants.worldDescription, WorldConstants.existingWorldSeed);
    }

    @AfterEach
    public void cleanup() {
        if (!TestConstants.keepData)
            worldService.getWorldRepository().deleteAllByName(WorldConstants.nonExistingWorldName);
    }

    @Test
    public void getWorld() throws Exception {
        World testWorld = worldTestUtil.getEnsuredInstance();
        String token = userTestUtil.authenticateTestUser();
        worldService.join(userTestUtil.getEnsuredUser(), testWorld);

        MvcResult result = mockMvc.perform(
                get(String.format("%s/id/%d", WorldConstants.endpoint, testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        WorldDto worldResponse = objectMapper.readValue(result.getResponse().getContentAsString(), WorldDto.class);

        assert worldResponse.getName().equals(WorldConstants.existingWorldName);
    }

    @Test
    public void getNonExistingWorld() throws Exception {
        String token = userTestUtil.authenticateTestUser();

        mockMvc.perform(
                get(String.format("%s/id/%d", WorldConstants.endpoint, 0))
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getNonExistingWorldBySeed() throws Exception {
        String token = userTestUtil.authenticateTestUser();

        mockMvc.perform(
                get(String.format("%s/seed/%s", WorldConstants.endpoint, WorldConstants.nonExistingWorldSeed))
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void joinWorld() throws Exception {
        World testWorld = worldTestUtil.getEnsuredInstance();

        User testUser = userTestUtil.getEnsuredUser();
        String token = userTestUtil.authenticateTestUser();

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
