package systems.nope.worldseed.stat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.TestConstants;
import systems.nope.worldseed.person.PersonTestUtil;
import systems.nope.worldseed.person.requests.AddPersonStatsheetRequest;
import systems.nope.worldseed.stat.requests.AddConstantStatRequest;
import systems.nope.worldseed.stat.requests.AddSynthesizedStatRequest;
import systems.nope.worldseed.stat.requests.UpdateConstantStatValueIntanceRequest;
import systems.nope.worldseed.stat.sheet.StatSheet;
import systems.nope.worldseed.stat.sheet.StatSheetService;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.util.requests.AddNamedResourceRequest;
import systems.nope.worldseed.world.World;
import systems.nope.worldseed.world.WorldTestUtil;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class StatTest {
    @Autowired
    private UserTestUtil userTestUtil;

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private PersonTestUtil personTestUtil;

    @Autowired
    private StatSheetService statSheetService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @BeforeEach
    public void ensureData() {
        userTestUtil.ensureTestuserExists();
        worldTestUtil.ensureTestWorldExists();
        personTestUtil.ensureTestPersonExists();

        Optional<StatSheet> nonExistingSheet =
                statSheetService.getStatSheetRepository().findByWorldAndName(worldTestUtil.getEnsuredInstance(),
                        StatSheetConstants.testSheetName);
        if (nonExistingSheet.isPresent())
            statSheetService.getStatSheetRepository().deleteAllByWorldAndName(worldTestUtil.getEnsuredInstance(), StatSheetConstants.testSheetName);
    }

    @AfterEach
    public void cleanup() {
        Optional<StatSheet> nonExistingSheet =
                statSheetService.getStatSheetRepository().findByWorldAndName(worldTestUtil.getEnsuredInstance(),
                        StatSheetConstants.testSheetName);
        if (nonExistingSheet.isPresent() && !TestConstants.keepData)
            statSheetService.getStatSheetRepository().deleteAllByWorldAndName(worldTestUtil.getEnsuredInstance(), StatSheetConstants.testSheetName);
    }

    @Test
    public void addStatSheetTest() throws Exception {
        mockMvc.perform(
                post(String.format("/stat-sheets/worlds/%d", worldTestUtil.getEnsuredInstance().getId()))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(builder.build().writeValueAsString(new AddNamedResourceRequest(StatSheetConstants.testSheetName)))
        ).andDo(print());
    }


    //    @Test
    public void addSheetToPerson() throws Exception {
        mockMvc.perform(
                post("/stat-sheet-mapping")
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddPersonStatsheetRequest(
                                                70,
                                                4
                                        )
                                )
                        )
        ).andDo(print());
    }

    //    @Test
    public void updatePersonConstantValue() throws Exception {
        mockMvc.perform(
                put(String.format("/stat-sheet-mapping/id/%d/constant-stat", 117))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new UpdateConstantStatValueIntanceRequest(100)
                                )
                        )
        ).andDo(print());
    }


}
