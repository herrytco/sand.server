package systems.nope.worldseed.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.world.WorldTestUtil;
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
    private UserTestUtil userTestUtil;

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @BeforeEach
    public void ensureData() {
        userTestUtil.ensureTestuserExists();
        worldTestUtil.ensureTestWorldExists();

        // delete test character (if existing)
        Optional<Person> testPerson = personRepository.findByWorldAndName(worldTestUtil.getEnsuredInstance(), PersonConstants.personName);
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
        World testWorld = worldTestUtil.ensureTestWorldExists();
        String token = userTestUtil.authenticateTestUser();

        createPerson(testWorld, token, PersonConstants.personName);
    }

    @Test
    public void getByApiTest() throws Exception {
        World testWorld = worldTestUtil.ensureTestWorldExists();
        String token = userTestUtil.authenticateTestUser();

        createPerson(testWorld, token, PersonConstants.personName);

        Optional<Person> personOptional = personRepository.findByWorldAndName(testWorld, PersonConstants.personName);

        assert personOptional.isPresent();

        Person person = personOptional.get();

        mockMvc.perform(
                get(String.format("%s/api/%s", PersonConstants.endpoint, person.getApiKey()))
                        .header("Authorization", String.format("Bearer %s", token))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    //    @Test
    public void createCustomPerson() throws Exception {
        Optional<World> testWorld = worldTestUtil.getWorldService().getWorldRepository().findByName(WorldConstants.konstoWorldName);
        Optional<String> token = userTestUtil.authenticateUser(UserConstants.herryName, UserConstants.herryPw);

        if (!testWorld.isPresent() || !token.isPresent())
            throw new Exception("Failed");

        createPerson(testWorld.get(), token.get(), "Yuki Li ");
    }

//        @Test
    public void getCustomPerson() throws Exception {
        String apiKey = "ozedawpqwxcksniuhlswtbfruonxfofexxnwbqlfjoihdwptutrorwlmunbrsibxvxfjdenkormbydautimannwinvoekfghpygohmpvmhwbkrujjvjwgoqmdqgndwoxhroydtpclreexzlcfvsbcnjgboddoolhjrcuilruwrcxsplkuzlbpfcrahfefjhfckdleytofqkfgfxwmiquycmrncubztxyzdvxbyrahqoffhigiuilrqwgpbidzwln";
        Optional<String> token = userTestUtil.authenticateUser(UserConstants.herryName, UserConstants.herryPw);

        if (!token.isPresent())
            throw new Exception("Failed");

        mockMvc.perform(
                get(String.format("%s/api/%s", PersonConstants.endpoint, apiKey))
                        .header("Authorization", String.format("Bearer %s", token.get()))
        ).andDo(print())
                .andExpect(status().isOk());
    }

}
