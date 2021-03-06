package systems.nope.worldseed.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import systems.nope.worldseed.repository.UserRepository;
import systems.nope.worldseed.token.TokenConstants;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.dto.request.RegistrationRequest;
import systems.nope.worldseed.dto.request.TokenRequest;
import systems.nope.worldseed.dto.response.TokenResponse;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class UserTestUtil {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User ensuredUser;

    public User ensureTestuserExists() {
        return ensureUserExists(UserConstants.name, UserConstants.nonExistingEmail, passwordEncoder.encode(UserConstants.password));
    }

    public User ensureUserExists(String name, String username, String password) {
        Optional<User> user = userRepository.findByEmail(UserConstants.nonExistingEmail);

        if (user.isEmpty()) {
            User userNew = new User(
                    name,
                    username,
                    passwordEncoder.encode(password)
            );
            userRepository.save(userNew);

            ensuredUser = userNew;
        } else
            ensuredUser = user.get();

        return ensuredUser;
    }

    public Optional<String> authenticateUser(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(
                post(TokenConstants.endpoint)
                        .content(builder.build().writeValueAsBytes(new TokenRequest(email, password)))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andReturn();

        if (result.getResponse().getStatus() == 200) {
            String response = result.getResponse().getContentAsString();
            TokenResponse tokenResponse = builder.build().readValue(response, TokenResponse.class);

            return Optional.of(tokenResponse.getToken());
        }

        return Optional.empty();
    }

    public String authenticateTestUser() throws Exception {
        MvcResult result = mockMvc.perform(
                post(TokenConstants.endpoint)
                        .content(builder.build().writeValueAsBytes(getTestuserTokenRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        String response = result.getResponse().getContentAsString();
        TokenResponse tokenResponse = builder.build().readValue(response, TokenResponse.class);

        return tokenResponse.getToken();
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

    public User getEnsuredUser() {
        return ensuredUser;
    }
}
