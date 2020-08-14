package systems.nope.worldseed.stat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.stat.model.StatSheet;
import systems.nope.worldseed.stat.model.StatValue;
import systems.nope.worldseed.stat.requests.AddConstantStatRequest;
import systems.nope.worldseed.stat.requests.AddSynthesizedStatRequest;
import systems.nope.worldseed.stat.responses.AddStatResponse;
import systems.nope.worldseed.stat.responses.StatSheetResponse;
import systems.nope.worldseed.util.requests.AddNamedResourceRequest;
import systems.nope.worldseed.world.World;
import systems.nope.worldseed.world.WorldService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stat-sheets")
public class StatSheetController {
    private final StatSheetService statSheetService;
    private final WorldService worldService;

    public StatSheetController(StatSheetService statSheetService, WorldService worldService) {
        this.statSheetService = statSheetService;
        this.worldService = worldService;
    }

    @GetMapping
    public List<StatSheet> all() {
        return statSheetService.getStatSheetRepository().findAll();
    }

    @GetMapping("/worlds/{worldId}")
    public ResponseEntity<?> findByName(
            @PathVariable int worldId,
            @RequestParam String name
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with ID '%d' not found.", worldId));

        World world = optionalWorld.get();

        Optional<StatSheet> referenceSheet = statSheetService.getStatSheetRepository().findByWorldAndName(world, name);

        if (referenceSheet.isPresent())
            return ResponseEntity.ok(new StatSheetResponse(referenceSheet.get()));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Stat Sheet '%s' does not exist.", name));
    }

    @PostMapping("/worlds/{worldId}/sheet/{sheetId}/synthesized-stat")
    public ResponseEntity<?> addSynthesizedStatToSheet(
            @PathVariable int worldId,
            @PathVariable int sheetId,
            @RequestBody AddSynthesizedStatRequest request
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with ID '%d' not found.", worldId));

        World world = optionalWorld.get();

        Optional<StatSheet> optionalStatSheet = statSheetService.getStatSheetRepository().findById(sheetId);

        if (optionalStatSheet.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("StatSheet with name '%s' already exists.", request.getName()));

        StatSheet sheet = optionalStatSheet.get();

        try {
            StatValue value = statSheetService.addSynthesizedStatValueToSheet(world, sheet, request.getName(), request.getNameShort(), request.getUnit(), request.getFormula());
            return ResponseEntity.ok(new AddStatResponse(value));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Stat with name '%s' already exists.", request.getName()));
        }
    }

    @PostMapping("/worlds/{worldId}/sheet/{sheetId}/constant-stat")
    public ResponseEntity<?> addConstantStatToSheet(
            @PathVariable int worldId,
            @PathVariable int sheetId,
            @RequestBody AddConstantStatRequest request
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with ID '%d' not found.", worldId));

        World world = optionalWorld.get();

        Optional<StatSheet> optionalStatSheet = statSheetService.getStatSheetRepository().findById(sheetId);

        if (optionalStatSheet.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("StatSheet with name '%s' already exists.", request.getName()));

        StatSheet sheet = optionalStatSheet.get();

        try {
            StatValue value = statSheetService.addConstantStatValueToSheet(world, sheet, request.getName(), request.getNameShort(), request.getUnit(), request.getInitialValue());
            return ResponseEntity.ok(new AddStatResponse(value));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Stat with name '%s' already exists.", request.getName()));
        }
    }

    @PostMapping("/worlds/{worldId}")
    public ResponseEntity<?> add(
            @PathVariable int worldId,
            @RequestBody AddNamedResourceRequest request
    ) {

        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with ID '%d' not found.", worldId));

        World world = optionalWorld.get();

        try {
            StatSheet sheetNew = statSheetService.add(world, request.getName());
            return ResponseEntity.ok(new StatSheetResponse((sheetNew)));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("StatSheet with name '%s' already exists.", request.getName()));
        }
    }
}

