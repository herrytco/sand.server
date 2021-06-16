package systems.nope.worldseed.controller.item;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.request.AddResourceToStatSheetRequest;
import systems.nope.worldseed.dto.request.UpdateConstantStatValueIntanceRequest;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.instance.item.StatValueItemInstanceConstant;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.service.WorldService;

@RestController
@RequestMapping("/item-stat-sheet-mapping")
public class ItemStatSheetController {
    private final ItemService itemService;
    private final StatSheetService statSheetService;
    private final WorldService worldService;

    public ItemStatSheetController(ItemService itemService, StatSheetService statSheetService, WorldService worldService) {
        this.itemService = itemService;
        this.statSheetService = statSheetService;
        this.worldService = worldService;
    }

    @Operation(summary = "Add a StatSheet identified by its id to an Item, identified by its id.")
    @PostMapping
    public void addStatsheetMapping(
            @RequestBody AddResourceToStatSheetRequest request
    ) {
        itemService.addStatSheetToItem(
                itemService.get(request.getTargetId()),
                statSheetService.get(request.getStatsheetId())
        );
    }

    @PutMapping("/worlds/{worldId}/instances/{instanceId}")
    public void updateStatValueInstance(
            @PathVariable Integer worldId,
            @PathVariable Integer instanceId,
            @RequestBody UpdateConstantStatValueIntanceRequest request
    ) {
        StatValueItemInstanceConstant target = itemService.getStatValueInstance(instanceId);
        World world = worldService.get(worldId);

        if (world.getId() != target.getWorld().getId())
            throw new NotFoundException(instanceId);

        itemService.updateStatValueInstance(target, request.getValueNew());
    }
}
