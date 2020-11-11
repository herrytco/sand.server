package systems.nope.worldseed.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import systems.nope.worldseed.dto.ItemDto;
import systems.nope.worldseed.dto.request.AddItemRequest;
import systems.nope.worldseed.dto.request.AddResourceToStatSheetRequest;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.repository.item.ItemRepository;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.stat.StatSheetTestUtil;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.world.WorldTestUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemTest {
    @Autowired
    private UserTestUtil userTestUtil;

    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private StatSheetTestUtil statSheetTestUtil;

    @Autowired
    private ItemTestUtil itemTestUtil;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void ensureData() {
        userTestUtil.ensureTestuserExists();
        worldTestUtil.ensureTestWorldExists();

        Optional<Item> optionalItem = itemRepository.findByWorldAndName(
                worldTestUtil.getEnsuredInstance(),
                ItemTestConstants.volatileItemName
        );
        optionalItem.ifPresent(item -> itemService.delete(item));
    }

    @AfterEach
    public void deleteVolatileItem() {
        Optional<Item> optionalItem = itemRepository.findByWorldAndName(
                worldTestUtil.getEnsuredInstance(),
                ItemTestConstants.volatileItemName
        );
        optionalItem.ifPresent(item -> itemService.delete(item));
    }

    @Test
    public void contextLoads() {
    }

    private String createAddItemRequest() throws JsonProcessingException {
        AddItemRequest request = new AddItemRequest();

        request.setName(ItemTestConstants.volatileItemName);
        request.setDescription(ItemTestConstants.volatileItemDescription);

        return builder.build().writeValueAsString(request);
    }

    @Test
    public void addTest() throws Exception {
        World testWorld = worldTestUtil.getEnsuredInstance();
        String token = userTestUtil.authenticateTestUser();

        MvcResult result = mockMvc.perform(
                post(String.format("%s/worlds/%d", ItemTestConstants.endpoint, testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(createAddItemRequest())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ItemDto addedItem = builder.build().readValue(result.getResponse().getContentAsString(), ItemDto.class);

        assertEquals(addedItem.getName(), ItemTestConstants.volatileItemName);
    }

    @Test
    public void addTwiceTest() throws Exception {
        World testWorld = worldTestUtil.getEnsuredInstance();
        String token = userTestUtil.authenticateTestUser();

        MvcResult result = mockMvc.perform(
                post(String.format("%s/worlds/%d", ItemTestConstants.endpoint, testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(createAddItemRequest())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ItemDto addedItem = builder.build().readValue(result.getResponse().getContentAsString(), ItemDto.class);
        assertEquals(addedItem.getName(), ItemTestConstants.volatileItemName);

        mockMvc.perform(
                post(String.format("%s/worlds/%d", ItemTestConstants.endpoint, testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(createAddItemRequest())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addSheetToPerson() throws Exception {
        StatSheet testSheet = statSheetTestUtil.ensureTestStatSheet();
        Item testItem = itemTestUtil.ensureTestItemExists();

        mockMvc.perform(
                post("/item-stat-sheet-mapping")
                        .header("Authorization", String.format("Bearer %s", userTestUtil.authenticateTestUser()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                builder.build().writeValueAsString(
                                        new AddResourceToStatSheetRequest(
                                                testItem.getId(),
                                                testSheet.getId()
                                        )
                                )
                        )
        ).andDo(print());

        System.out.println("done");
    }
}
