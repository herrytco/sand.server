package systems.nope.worldseed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import systems.nope.worldseed.token.TokenConstants;
import systems.nope.worldseed.user.User;
import systems.nope.worldseed.user.UserConstants;
import systems.nope.worldseed.user.UserRepository;
import systems.nope.worldseed.user.requests.RegistrationRequest;
import systems.nope.worldseed.user.requests.TokenRequest;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class Authenticator {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void ensureTestuserExists() {
        Optional<User> user = userRepository.findByEmail(UserConstants.nonExistingEmail);

        if (user.isEmpty()) {
            User userNew = new User(
                    UserConstants.name,
                    UserConstants.nonExistingEmail,
                    passwordEncoder.encode(UserConstants.password)
            );
            userRepository.save(userNew);
        }
    }

    public String authenticateTestUser() throws Exception {
        MvcResult result = mockMvc.perform(
                get(TokenConstants.endpoint)
                        .content(builder.build().writeValueAsBytes(getTestuserTokenRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    public static TokenRequest getTestuserTokenRequest() {
        return new TokenRequest(
                UserConstants.nonExistingEmail,
                UserConstants.password
        );
    }

    public static RegistrationRequest getTestuserRegistrationRequest() {
        return new RegistrationRequest(
                UserConstants.name,
                UserConstants.nonExistingEmail,
                UserConstants.password
        );
    }
}
