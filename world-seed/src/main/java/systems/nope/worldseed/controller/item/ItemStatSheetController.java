package systems.nope.worldseed.controller.item;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.dto.request.AddResourceToStatSheetRequest;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.service.StatSheetService;

@RestController
@RequestMapping("/item-stat-sheet-mapping")
public class ItemStatSheetController {
    private final ItemService itemService;
    private final StatSheetService statSheetService;

    public ItemStatSheetController(ItemService itemService, StatSheetService statSheetService) {
        this.itemService = itemService;
        this.statSheetService = statSheetService;
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
}
