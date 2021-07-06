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
import systems.nope.worldseed.dto.request.AddConstantStatRequest;
import systems.nope.worldseed.dto.request.AddSynthesizedStatRequest;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.repository.stat.StatValueConstantRepository;
import systems.nope.worldseed.repository.stat.StatValueSynthesizedRepository;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.world.WorldTestUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StatValueTest {

    @Autowired
    private UserTestUtil userTestUtil;

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private StatSheetTestUtil statSheetTestUtil;

    @Autowired
    private StatValueConstantRepository statValueConstantRepository;

    @Autowired
    private StatValueSynthesizedRepository statValueSynthesizedRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @BeforeEach
    public void ensureData() {
        userTestUtil.ensureTestuserExists();
        worldTestUtil.ensureTestWorldExists();
        statSheetTestUtil.ensureTestStatSheet();

        statValueConstantRepository.deleteAllByWorldAndName(worldTestUtil.getEnsuredInstance(), StatSheetConstants.testConstantStatName);
        statValueSynthesizedRepository.deleteAllByWorldAndName(worldTestUtil.getEnsuredInstance(), StatSheetConstants.testSyntheticStatName);
    }

    @AfterEach
    public void cleanup() {
        if (!TestConstants.keepData) {
            statValueConstantRepository.deleteAllByWorldAndName(worldTestUtil.getEnsuredInstance(), StatSheetConstants.testConstantStatName);
            statValueSynthesizedRepository.deleteAllByWorldAndName(worldTestUtil.getEnsuredInstance(), StatSheetConstants.testSyntheticStatName);
        }
    }

    @Test
    public void addConstantStat() throws Exception {
        StatSheet testSheet = statSheetTestUtil.ensureStatSheet(StatSheetConstants.testSheetName);

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
                                                StatSheetConstants.testConstantStatDefault,
                                                false
                                        )
                                )
                        )
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addSynthesizedStat() throws Exception {
        mockMvc.perform(
                post(String.format("/stat-sheets/worlds/%d/sheet/%d/synthesized-stat", worldTestUtil.getEnsuredInstance().getId(), 4))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddSynthesizedStatRequest(
                                                StatSheetConstants.testSyntheticStatName,
                                                StatSheetConstants.testSyntheticStatName,
                                                StatSheetConstants.testConstantStatUnit,
                                                StatSheetConstants.testSyntheticStatFormula,
                                                false
                                        )
                                )
                        )
        ).andDo(print());
    }
}
