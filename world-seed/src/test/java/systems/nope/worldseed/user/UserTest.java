package systems.nope.worldseed.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.repository.UserRepository;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the UserController only. All tests require the test user to NOT exist before and only involve
 * the User* classes
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTestUtil userTestUtil;

    private String getNewUserDetails() throws JsonProcessingException {
        return builder.build().writeValueAsString(UserTestUtil.getTestuserRegistrationRequest());
    }

    @BeforeEach
    public void deleteJUnitUser() {
        userRepository.deleteByEmail(UserConstants.nonExistingEmail);
    }

    @AfterEach
    public void deleteJUnitUserAfter() {
        deleteJUnitUser();
    }

    /**
     * tests if the statuscode for my worlds is correct (200)
     * does NOT test the content
     *
     * @throws Exception
     */
    @Test
    public void myWorldTest() throws Exception {
        userTestUtil.ensureTestuserExists();

        Optional<User> testUser = userRepository.findByEmail(UserConstants.nonExistingEmail);
        assert testUser.isPresent();

        String token = userTestUtil.authenticateTestUser();

        mockMvc.perform(
                get(String.format("%s/id/%d/worlds", UserConstants.endpoint, testUser.get().getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
    public void ensureHerryUserExists() {
        userTestUtil.ensureUserExists(UserConstants.herryName, UserConstants.herryName, UserConstants.herryPw);
    }

    @Test
    public void getToken() throws Exception {
        userTestUtil.ensureTestuserExists();
        String token = userTestUtil.authenticateTestUser();
        System.out.println("TOKEN: " + token);
    }

    @Test
    public void getUser() throws Exception {
        userTestUtil.ensureTestuserExists();
        String token = userTestUtil.authenticateTestUser();

        Optional<User> testUser = userRepository.findByEmail(UserConstants.nonExistingEmail);
        assert testUser.isPresent();

        mockMvc.perform(
                get(String.format("%s/id/%d", UserConstants.endpoint, testUser.get().getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void registerUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(getNewUserDetails())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void repeatedlyRegisterUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(getNewUserDetails())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/users")
                        .content(getNewUserDetails())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }


}
