package systems.nope.worldseed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.dto.ItemDto;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.repository.item.ItemRepository;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.service.WorldService;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {

    public ItemController(ItemRepository itemRepository, StatSheetService statSheetService,
                          WorldService worldService) {
        this.itemRepository = itemRepository;
        this.statSheetService = statSheetService;
        this.worldService = worldService;
    }

    private final ItemRepository itemRepository;
    private final StatSheetService statSheetService;
    private final WorldService worldService;

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
        List<ItemDto> result = new LinkedList<>();

        for (Item item : items) {
            Set<StatSheet> requiredSheets = item.getRequiredStatSheets();
            requiredSheets = statSheetService.groundStatSheets(requiredSheets);

            ItemDto dto = ItemDto.fromItem(item);
            dto.setRequiredStatSheets(
                    requiredSheets.stream().map(StatSheet::getId).collect(Collectors.toSet())
            );

            result.add(dto);
        }

        return result;
    }
}
