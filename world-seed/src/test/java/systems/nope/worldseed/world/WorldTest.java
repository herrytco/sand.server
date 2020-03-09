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
import systems.nope.worldseed.user.User;
import systems.nope.worldseed.user.UserConstants;
import systems.nope.worldseed.user.UserRepository;
import systems.nope.worldseed.world.requests.NewWorldRequest;

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
    private UserRepository userRepository;

    @Autowired
    private WorldRepository worldRepository;

    private String newWorldContent() throws JsonProcessingException {
        NewWorldRequest request = new NewWorldRequest(
                WorldConstants.nonExistingWorldName,
                WorldConstants.worldDescription.substring(0, 256)
        );

        return builder.build().writeValueAsString(request);
    }

    @BeforeEach
    public void createTestUser() {
        userRepository.deleteByEmail(UserConstants.nonExistingEmail);
        User userJunit = new User(UserConstants.name, UserConstants.nonExistingEmail, UserConstants.password);
        userRepository.save(userJunit);

        worldRepository.deleteAllByName(WorldConstants.nonExistingWorldName);
    }


    @Test
    public void createWorldTest() throws Exception {
        mockMvc.perform(
                post(WorldConstants.endpoint)
                        .content(newWorldContent())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void repeatedlyCreateWorldTest() throws Exception {
        mockMvc.perform(
                post(WorldConstants.endpoint)
                        .content(newWorldContent())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
        
        mockMvc.perform(
                post(WorldConstants.endpoint)
                        .content(newWorldContent())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }


}
