package systems.nope.worldseed.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the UserController only. All tests require the test user to NOT exist before and only involve
 * the User* classes
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserCustomTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTestUtil userTestUtil;


    /**
     * tests if the statuscode for my worlds is correct (200)
     * does NOT test the content
     *
     * @throws Exception
     */
    @Test
    public void myWorldTest() throws Exception {

        Optional<User> testUser = userRepository.findByEmail("m1herold@edu.aau.at");
        assert testUser.isPresent();

        Optional<String> token = userTestUtil.authenticateUser(
                "m1herold@edu.aau.at",
                "hallihallo"
        );

        assert token.isPresent();

        mockMvc.perform(
                get(String.format("%s/id/%d/worlds", UserConstants.endpoint, testUser.get().getId()))
                        .header("Authorization", String.format("Bearer %s", token.get()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }
}
