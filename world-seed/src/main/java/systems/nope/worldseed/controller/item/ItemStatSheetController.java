package systems.nope.worldseed.controller.item;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.request.stat.AddResourceToStatSheetRequest;
import systems.nope.worldseed.dto.request.stat.UpdateConstantStatValueInstanceRequest;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceConstant;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.service.StatValueInstanceService;
import systems.nope.worldseed.service.WorldService;

@RestController
@RequestMapping("/item-stat-sheet-mapping")
public class ItemStatSheetController {
    private final ItemService itemService;
    private final StatSheetService statSheetService;
    private final WorldService worldService;
    private final StatValueInstanceService statValueInstanceService;

    public ItemStatSheetController(ItemService itemService, StatSheetService statSheetService,
                                   WorldService worldService, StatValueInstanceService statValueInstanceService) {
        this.itemService = itemService;
        this.statSheetService = statSheetService;
        this.worldService = worldService;
        this.statValueInstanceService = statValueInstanceService;
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
            @RequestBody UpdateConstantStatValueInstanceRequest request
    ) {
        StatValueInstanceConstant target = statValueInstanceService.getConstant(instanceId);
        World world = worldService.get(worldId);

        if (world.getId() != target.getWorld().getId())
            throw new NotFoundException(instanceId);

        statValueInstanceService.update(target, request.getValueNew(), request.getModifierNew());
    }
}
