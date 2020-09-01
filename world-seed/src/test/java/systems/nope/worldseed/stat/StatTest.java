package systems.nope.worldseed.stat;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
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
import systems.nope.worldseed.dto.request.*;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.person.PersonTestUtil;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.world.WorldTestUtil;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private StatSheetTestUtil statSheetTestUtil;

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
            statSheetService.getStatSheetRepository().delete(nonExistingSheet.get());
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

    @Test
    public void addSheetToPerson() throws Exception {
        StatSheet testSheet = statSheetTestUtil.ensureTestStatSheet();
        Person testPerson = personTestUtil.ensureTestPersonExists();

        mockMvc.perform(
                post("/stat-sheet-mapping")
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddPersonStatsheetRequest(
                                                testPerson.getId(),
                                                testSheet.getId()
                                        )
                                )
                        )
        ).andDo(print());
    }

    @Test
    public void addSheetToPersonAndAddNewConstantStat() throws Exception {
        StatSheet testSheet = statSheetTestUtil.ensureTestStatSheet();
        Person testPerson = personTestUtil.ensureTestPersonExists();

        // assign sheet
        mockMvc.perform(
                post("/stat-sheet-mapping")
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddPersonStatsheetRequest(
                                                testPerson.getId(),
                                                testSheet.getId()
                                        )
                                )
                        )
        ).andDo(print());

        // add new constant stat to sheet
        mockMvc.perform(
                post(String.format("/stat-sheets/worlds/%d/sheet/%d/constant-stat", worldTestUtil.getEnsuredInstance().getId(), testSheet.getId()))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddConstantStatRequest(
                                                StatSheetConstants.testConstantStatName,
                                                StatSheetConstants.testConstantStatName,
                                                StatSheetConstants.testConstantStatUnit,
                                                StatSheetConstants.testConstantStatDefault
                                        )
                                )
                        )
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addSheetToPersonAndAddNewSynthesizedStat() throws Exception {
        StatSheet testSheet = statSheetTestUtil.ensureTestStatSheet();
        Person testPerson = personTestUtil.ensureTestPersonExists();

        // assign sheet
        mockMvc.perform(
                post("/stat-sheet-mapping")
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddPersonStatsheetRequest(
                                                testPerson.getId(),
                                                testSheet.getId()
                                        )
                                )
                        )
        ).andDo(print());

        // add new synthesized stat to sheet
        mockMvc.perform(
                post(String.format("/stat-sheets/worlds/%d/sheet/%d/synthesized-stat", worldTestUtil.getEnsuredInstance().getId(), testSheet.getId()))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddSynthesizedStatRequest(
                                                StatSheetConstants.testSyntheticStatName,
                                                StatSheetConstants.testSyntheticStatName,
                                                StatSheetConstants.testConstantStatUnit,
                                                StatSheetConstants.testSyntheticStatFormula
                                        )
                                )
                        )
        ).andDo(print())
        .andExpect(status().isOk());
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
