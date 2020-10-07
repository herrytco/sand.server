package systems.nope.worldseed.tileset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import systems.nope.worldseed.dto.TilesetDto;
import systems.nope.worldseed.dto.request.AddTilesetRequest;
import systems.nope.worldseed.dto.request.UpdateTileRequest;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.tile.Tileset;
import systems.nope.worldseed.repository.tile.TilesetRepository;
import systems.nope.worldseed.service.tile.TilesetService;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.world.WorldTestUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TilesetTest {
    @Autowired
    private UserTestUtil userTestUtil;

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private TilesetService tilesetService;

    @Autowired
    private TilesetRepository tilesetRepository;

    public AddTilesetRequest createAddRequest() {
        return new AddTilesetRequest(
                TileTestConstants.tilesetTestName,
                TileTestConstants.tileSize,
                TileTestConstants.tileSize
        );
    }

    @BeforeEach
    public void setup() {
        userTestUtil.ensureTestuserExists();
        worldTestUtil.ensureTestWorldExists();
    }

    @AfterEach
    public void teardown() throws IOException {
        World testWorld = worldTestUtil.getEnsuredInstance();
        Optional<Tileset> tilesetOptional = tilesetRepository.findByWorldAndName(testWorld, TileTestConstants.tilesetTestName);

        if (tilesetOptional.isPresent()) {
            tilesetService.delete(tilesetOptional.get().getId());
        }
    }

    private TilesetDto add() throws Exception {
        ClassPathResource r = new ClassPathResource("test/grassland_tileset_128.png");

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.txt",
                "image/png",
                Files.readAllBytes(r.getFile().toPath())
        );

        MvcResult result = mockMvc.perform(
                multipart(String.format("/tile-sets/worlds/%d", worldTestUtil.getEnsuredInstance().getId()))
                        .file(file)
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(builder.build().writeValueAsString(createAddRequest()))
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        return builder.build().readValue(result.getResponse().getContentAsString(), TilesetDto.class);
    }

    UpdateTileRequest createUpdateTileRequest() {
        return new UpdateTileRequest(
                TileTestConstants.tileName,
                TileTestConstants.tileDescriptionShort,
                TileTestConstants.tileDescriptionLong,
                TileTestConstants.tileColor
        );
    }

    @Test
    public void updateFirstTileTest() throws Exception {
        TilesetDto dto = add();

        // update tile request
        mockMvc.perform(
                put(String.format("/tile-sets/%d/tile/0", dto.getId()))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(builder.build().writeValueAsString(createUpdateTileRequest()))
        ).andDo(print())
                .andExpect(status().isOk());

        System.out.println("Done");
    }

    @Test
    public void addTileSetTest() throws Exception {
        TilesetDto dto = add();

        mockMvc.perform(
                get(String.format("/tile-sets/%d/tile/0/tile.png", dto.getId()))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addAndDeleteTileSetTest() throws Exception {
        TilesetDto tilesetDto = add();

        mockMvc.perform(
                delete(String.format("/tile-sets/id/%d", tilesetDto.getId()))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
        ).andDo(print())
                .andExpect(status().isOk());
    }
}
