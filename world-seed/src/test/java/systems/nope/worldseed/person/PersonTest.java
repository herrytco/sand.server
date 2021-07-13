package systems.nope.worldseed.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import systems.nope.worldseed.dto.person.PersonDto;
import systems.nope.worldseed.dto.request.AddNamedResourceRequest;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.repository.person.PersonRepository;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.util.file.PortraitFileUtil;
import systems.nope.worldseed.world.WorldTestUtil;
import systems.nope.worldseed.model.World;

import java.nio.file.Files;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PortraitFileUtil portraitFileUtil;

    @BeforeEach
    public void ensureData() {
        userTestUtil.ensureTestuserExists();
        worldTestUtil.ensureTestWorldExists();

        // delete test character (if existing)
        Optional<Person> testPerson = personRepository.findByWorldAndName(worldTestUtil.getEnsuredInstance(), PersonConstants.personName);
        testPerson.ifPresent(person -> personRepository.delete(person));
    }

    @AfterEach
    public void cleanup() {
        personRepository.deleteAllByName(PersonConstants.personName);
        personRepository.deleteAllByName(PersonConstants.personName2);
        personRepository.deleteAllByName(PersonConstants.personName3);
    }

    private PersonDto createPerson(String name) throws Exception {
        World testWorld = worldTestUtil.getEnsuredInstance();
        String token = userTestUtil.authenticateTestUser();

        AddNamedResourceRequest createPersonRequest = new AddNamedResourceRequest(name);

        MvcResult result = mockMvc.perform(
                post(String.format("%s/world/%d", PersonConstants.endpoint, testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(builder.build().writeValueAsString(createPersonRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        PersonDto addedPerson = objectMapper.readValue(result.getResponse().getContentAsString(), PersonDto.class);

        assertEquals(name, addedPerson.getName());

        return addedPerson;
    }

    @Test
    public void createPersonTest() throws Exception {
        PersonDto p = createPerson(PersonConstants.personName);
    }

    @Test
    public void createMultiplePersonTest() throws Exception {
        String token = userTestUtil.authenticateTestUser();

        PersonDto p = createPerson(PersonConstants.personName);
        PersonDto p2 = createPerson(PersonConstants.personName2);
        PersonDto p3 = createPerson(PersonConstants.personName3);

        MvcResult result = mockMvc.perform(
                get(String.format("%s?id=%d&id=%d&id=%d", PersonConstants.endpoint, p.getId(), p2.getId(), p3.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        PersonDto[] ps = objectMapper.readValue(result.getResponse().getContentAsString(), PersonDto[].class);

        assertEquals(3, ps.length);
    }

    @Test
    public void getByApiTest() throws Exception {
        World testWorld = worldTestUtil.ensureTestWorldExists();
        String token = userTestUtil.authenticateTestUser();

        createPerson(PersonConstants.personName);

        Optional<Person> personOptional = personRepository.findByWorldAndName(testWorld, PersonConstants.personName);
        assert personOptional.isPresent();

        Person person = personOptional.get();

        mockMvc.perform(
                get(String.format("%s/api/%s", PersonConstants.endpoint, person.getApiKey()))
                        .header("Authorization", String.format("Bearer %s", token))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void uploadPortraitTest() throws Exception {
        World testWorld = worldTestUtil.ensureTestWorldExists();
        String token = userTestUtil.authenticateTestUser();

        createPerson(PersonConstants.personName);

        Optional<Person> personOptional = personRepository.findByWorldAndName(testWorld, PersonConstants.personName);
        assert personOptional.isPresent();

        Person person = personOptional.get();

        ClassPathResource r = new ClassPathResource("test/valid_portrait.png");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "valid_portrait.png",
                "image/png",
                Files.readAllBytes(r.getFile().toPath())
        );

        mockMvc.perform(
                multipart(
                        String.format("%s/%d/portrait", PersonConstants.endpoint, person.getId())
                )
                        .file(file)
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());

        portraitFileUtil.deleteFolder(testWorld, person, "");
    }

}
