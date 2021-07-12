package systems.nope.worldseed.world;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.repository.WorldRepository;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.dto.request.NewWorldRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WorldTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private WorldRepository worldRepository;

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private UserTestUtil userTestUtil;

    private String newWorldContent() throws JsonProcessingException {
        NewWorldRequest request = new NewWorldRequest(
                WorldConstants.nonExistingWorldName,
                WorldConstants.worldDescription
        );

        return builder.build().writeValueAsString(request);
    }

    @BeforeEach
    public void createTestUser() {
        userTestUtil.ensureTestuserExists();
        worldRepository.deleteAllByName(WorldConstants.nonExistingWorldName);
    }

    @Test
    public void deleteAndAddSeedTest() throws Exception {
        World worldTest = worldTestUtil.ensureTestWorldExists();

        mockMvc.perform(
                get(String.format("%s/seed/%s", WorldConstants.endpoint, worldTest.getSeed()))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void createWorldWithoutTokenUnauthorized() throws Exception {
        mockMvc.perform(
                post(WorldConstants.endpoint)
                        .content(newWorldContent())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void createWorldTest() throws Exception {
        mockMvc.perform(
                post(WorldConstants.endpoint)
                        .content(newWorldContent())
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getWorldTest() throws Exception {
        mockMvc.perform(
                get(WorldConstants.endpoint + "/id/604")
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void repeatedlyCreateWorldTest() throws Exception {
        mockMvc.perform(
                post(WorldConstants.endpoint)
                        .content(newWorldContent())
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                post(WorldConstants.endpoint)
                        .content(newWorldContent())
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }
}
