package systems.nope.worldseed.stat;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.world.WorldTestUtil;
import systems.nope.worldseed.dto.request.AddPersonStatsheetRequest;
import systems.nope.worldseed.dto.request.AddConstantStatRequest;
import systems.nope.worldseed.dto.request.AddSynthesizedStatRequest;
import systems.nope.worldseed.dto.request.UpdateConstantStatValueIntanceRequest;
import systems.nope.worldseed.repository.stat.StatSheetRepository;
import systems.nope.worldseed.dto.request.AddNamedResourceRequest;
import systems.nope.worldseed.model.World;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class StatTestCustom {
    @Autowired
    private UserTestUtil userTestUtil;

    @Autowired
    private WorldTestUtil worldinator;

    @Autowired
    private StatSheetRepository statSheetRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    private static final String seed = "355772";

    private static final String statSheetName = "Player Sheet 1";

    private World world;

    @BeforeEach
    public void ensureData() {
        userTestUtil.ensureTestuserExists();
        world = worldinator.ensureWorldExists("Riverlands RPG", "Lorem Ipsum Si Dolor Amet", seed);
    }

//    @Test
    public void addStatSheet() throws Exception {
        mockMvc.perform(
                post(String.format("/stat-sheets/worlds/%d", world.getId()))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(builder.build().writeValueAsString(new AddNamedResourceRequest("Attack Modifiers")))
        ).andDo(print());
    }

//    @Test
    public void addConstantStat() throws Exception {
        String statNameShort = "WLL";
        String statName = "Will";
        int initialValue = 90;

        mockMvc.perform(
                post(String.format("/stat-sheets/worlds/%d/sheet/%d/constant-stat", world.getId(), 2))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddConstantStatRequest(
                                                statName,
                                                statNameShort,
                                                null,
                                                initialValue
                                        )
                                )
                        )
        ).andDo(print());
    }

//    @Test
    public void addSynthesizedStat() throws Exception {
        String statNameShort = "MM";
        String statName = "Magic Modifier";
        String formula = "( 0.1 * INT )";

        mockMvc.perform(
                post(String.format("/stat-sheets/worlds/%d/sheet/%d/synthesized-stat", world.getId(), 4))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddSynthesizedStatRequest(
                                                statName,
                                                statNameShort,
                                                null,
                                                formula
                                        )
                                )
                        )
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
