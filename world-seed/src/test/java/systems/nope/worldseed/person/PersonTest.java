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
import systems.nope.worldseed.world.WorldConstants;

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

    private void createPerson(World world, String token, String name) throws Exception {
        CreatePersonRequest createPersonRequest = new CreatePersonRequest(name);

        mockMvc.perform(
                post(String.format("%s/world/%d", PersonConstants.endpoint, world.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(builder.build().writeValueAsString(createPersonRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void createPersonTest() throws Exception {
        World testWorld = worldinator.ensureTestWorldExists();
        String token = authenticator.authenticateTestUser();

        createPerson(testWorld, token, PersonConstants.personName);
    }

    @Test
    public void createCustomPerson() throws Exception {
        Optional<World> testWorld = worldinator.getWorldService().getWorldRepository().findByName(WorldConstants.konstoWorldName);
        Optional<String> token = authenticator.authenticateUser(UserConstants.herryName, UserConstants.herryPw);

        if(!testWorld.isPresent() || !token.isPresent())
            throw new Exception("Failed");

        createPerson(testWorld.get(), token.get(), "Pit");
    }

    @Test
    public void getCustomPerson() throws Exception {
        String apiKey = "ywnnoaruoccxrkjegbetdwlwhrvamgfcvzbgijotuiygfighuckecuckbenvggcgfbmouygbumhletznypscdrywvfaphzatgihctxrwnfzufxicialbsyiaihqjaztfsrhjwfplbjtcnpscjofozdyjhmedgfofnoggtlhrxclylwlylyznfzzkheyaulsoogfshgjizdbuzwzfspqjmgodyfpslmqtnlljgpvlovixccqibolldqslqybrgvmx";
        Optional<String> token = authenticator.authenticateUser(UserConstants.herryName, UserConstants.herryPw);

        if(!token.isPresent())
            throw new Exception("Failed");

        mockMvc.perform(
                get(String.format("%s/api/%s", PersonConstants.endpoint, apiKey))
                        .header("Authorization", String.format("Bearer %s", token.get()))
        ).andDo(print())
                .andExpect(status().isOk());
    }

}
