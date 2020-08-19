package systems.nope.worldseed.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.dto.request.AddNamedResourceRequest;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.repository.PersonRepository;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.world.WorldTestUtil;
import systems.nope.worldseed.model.World;

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

    private void createPerson() throws Exception {
        World testWorld = worldTestUtil.ensureTestWorldExists();
        String token = userTestUtil.authenticateTestUser();

        AddNamedResourceRequest createPersonRequest = new AddNamedResourceRequest(PersonConstants.personName);

        mockMvc.perform(
                post(String.format("%s/world/%d", PersonConstants.endpoint, testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(builder.build().writeValueAsString(createPersonRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void createPersonTest() throws Exception {
        createPerson();
    }

    @Test
    public void getByApiTest() throws Exception {
        World testWorld = worldTestUtil.ensureTestWorldExists();
        String token = userTestUtil.authenticateTestUser();

        createPerson();

        Optional<Person> personOptional = personRepository.findByWorldAndName(testWorld, PersonConstants.personName);
        assert personOptional.isPresent();

        Person person = personOptional.get();

        mockMvc.perform(
                get(String.format("%s/api/%s", PersonConstants.endpoint, person.getApiKey()))
                        .header("Authorization", String.format("Bearer %s", token))
        ).andDo(print())
                .andExpect(status().isOk());
    }

}
