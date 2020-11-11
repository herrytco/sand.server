package systems.nope.worldseed.controller.item;

import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.ItemDto;
import systems.nope.worldseed.dto.request.AddItemRequest;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.repository.item.ItemRepository;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.service.WorldService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {

    public ItemController(ItemRepository itemRepository, ItemService itemService, WorldService worldService) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        this.worldService = worldService;
    }

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final WorldService worldService;

    @PostMapping("/worlds/{worldId}")
    private ItemDto add(
            @PathVariable Integer worldId,
            @RequestBody AddItemRequest request
    ) {
        return ItemDto.fromItem(itemService.add(
                worldService.get(worldId),
                request.getName(),
                request.getDescription()
        ));
    }

    @GetMapping("/worlds/{worldId}")
    private List<ItemDto> forWorld(
            @PathVariable Integer worldId
    ) {
        return itemRepository.findAllByWorld(worldService.get(worldId))
                .stream().map(ItemDto::fromItem).collect(Collectors.toList());
    }

    @GetMapping
    private List<ItemDto> all() {
        List<Item> items = itemRepository.findAll();

        return items.stream().map(ItemDto::fromItem).collect(Collectors.toList());
    }
}
