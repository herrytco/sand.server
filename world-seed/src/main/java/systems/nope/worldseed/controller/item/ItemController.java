package systems.nope.worldseed.controller.item;

import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.ActionDto;
import systems.nope.worldseed.dto.ItemDto;
import systems.nope.worldseed.dto.request.AddActionRequest;
import systems.nope.worldseed.dto.request.AddItemRequest;
import systems.nope.worldseed.dto.stat.StatSheetDto;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.repository.item.ItemRepository;
import systems.nope.worldseed.service.ActionService;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.service.WorldService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {

    public ItemController(ItemRepository itemRepository, ItemService itemService, WorldService worldService,
                          ActionService actionService, StatSheetService statSheetService) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        this.worldService = worldService;
        this.actionService = actionService;
        this.statSheetService = statSheetService;
    }

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final WorldService worldService;
    private final ActionService actionService;
    private final StatSheetService statSheetService;

    @PostMapping("/{itemId}/actions")
    public ActionDto add(
            @PathVariable Integer itemId,
            @RequestBody AddActionRequest request
    ) {
        return ActionDto.fromAction(
                actionService.add(
                        itemService.get(itemId),
                        worldService.get(request.getWorldId()),
                        request.getName(),
                        request.getDescription(),
                        request.getFormula(),
                        request.getInvokeMessage()
                )
        );
    }

    @GetMapping("/{itemId}/actions")
    public List<ActionDto> actionsForItem(
            @PathVariable Integer itemId
    ) {
        return actionService.allForItem(
                itemService.get(itemId)
        ).stream().map(ActionDto::fromAction)
                .collect(Collectors.toList());
    }

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

    @GetMapping("/worlds/{worldId}/items/{itemId}")
    private ItemDto byId(
            @PathVariable Integer worldId,
            @PathVariable Integer itemId
    ) {
        Item result = itemService.get(itemId);

        if (result.getWorld().getId() != worldId)
            throw new NotFoundException(worldId);

        return ItemDto.fromItem(result);
    }

    @GetMapping
    private List<ItemDto> all() {
        List<Item> items = itemRepository.findAll();

        return items.stream().map(ItemDto::fromItem).collect(Collectors.toList());
    }
}
