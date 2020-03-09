package systems.nope.worldseed.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.Authenticator;
import systems.nope.worldseed.user.requests.RegistrationRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private UserRepository userRepository;


    private String getNewUserDetails() throws JsonProcessingException {
        return builder.build().writeValueAsString(Authenticator.getNewUserDetails());
    }

    @BeforeEach
    public void deleteJUnitUser() {
        System.out.println(String.format("Deleting User with Email '%s'", UserConstants.nonExistingEmail));
        userRepository.deleteByEmail(UserConstants.nonExistingEmail);
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
