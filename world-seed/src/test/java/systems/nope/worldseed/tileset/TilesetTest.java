package systems.nope.worldseed.tileset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import systems.nope.worldseed.dto.TilesetDto;
import systems.nope.worldseed.dto.request.AddNamedResourceRequest;
import systems.nope.worldseed.dto.request.AddTilesetRequest;
import systems.nope.worldseed.stat.StatSheetConstants;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.world.WorldTestUtil;

import java.nio.file.Files;

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

    @Test
    public void addStatSheetTest() throws Exception {
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

        TilesetDto tileset = builder.build().readValue(result.getResponse().getContentAsString(), TilesetDto.class);

        mockMvc.perform(
                delete(String.format("/tile-sets/id/%d", tileset.getId()))
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(builder.build().writeValueAsString(createAddRequest()))
        ).andDo(print())
                .andExpect(status().isOk());
    }
}
