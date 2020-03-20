package systems.nope.worldseed.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.Authenticator;
import systems.nope.worldseed.Worldinator;
import systems.nope.worldseed.person.requests.CreatePersonRequest;
import systems.nope.worldseed.user.UserConstants;
import systems.nope.worldseed.world.World;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonTest {

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private Worldinator worldinator;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @BeforeEach
    public void ensureData() {
        authenticator.ensureTestuserExists();
        worldinator.ensureTestWorldExists();

        Optional<Person> testPerson = personRepository.findByName(PersonConstants.personName);
        testPerson.ifPresent(person -> personRepository.delete(person));
    }

    @Test
    public void createPersonTest() throws Exception {
        World testWorld = worldinator.ensureTestWorldExists();
        String token = authenticator.authenticateTestUser();

        CreatePersonRequest createPersonRequest = new CreatePersonRequest(PersonConstants.personName);

        mockMvc.perform(
                post(String.format("%s/world/%d", PersonConstants.endpoint, testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(builder.build().writeValueAsString(createPersonRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

}
