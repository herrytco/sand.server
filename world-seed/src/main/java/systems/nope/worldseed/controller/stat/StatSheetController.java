package systems.nope.worldseed.controller.stat;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.StatSheetDto;
import systems.nope.worldseed.dto.StatValueDto;
import systems.nope.worldseed.dto.StatValueSynthesizedDto;
import systems.nope.worldseed.dto.request.AddConstantStatRequest;
import systems.nope.worldseed.dto.request.AddSynthesizedStatRequest;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.dto.request.AddNamedResourceRequest;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.WorldService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/stat-sheets")
public class StatSheetController {
    private final StatSheetService statSheetService;
    private final WorldService worldService;

    public StatSheetController(StatSheetService statSheetService, WorldService worldService) {
        this.statSheetService = statSheetService;
        this.worldService = worldService;
    }

    @Operation(summary = "Get a set of StatSheets identified by their ids.")
    @GetMapping
    public List<StatSheetDto> multiple(
            @RequestParam(name = "id") Integer[] ids
    ) {
        return Stream.of(ids).map(this::one).collect(Collectors.toList());
    }

    @Operation(summary = "Get single StatSheet identified by its id.")
    @GetMapping("/id/{id}")
    public StatSheetDto one(
            @PathVariable int id
    ) {
        Optional<StatSheet> optionalStatSheet = statSheetService.findById(id);

        if (optionalStatSheet.isEmpty())
            throw new NotFoundException(id);

        return StatSheetDto.fromStatSheet(optionalStatSheet.get());
    }

    @Operation(summary = "Get single StatSheet identified by its name.")
    @GetMapping("/worlds/{worldId}")
    public StatSheetDto findByName(
            @PathVariable int worldId,
            @RequestParam String name
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            throw new NotFoundException(worldId);

        World world = optionalWorld.get();

        Optional<StatSheet> optionalStatSheet = statSheetService.getStatSheetRepository().findByWorldAndName(world, name);

        if (optionalStatSheet.isEmpty())
            throw new NotFoundException(name);

        return StatSheetDto.fromStatSheet(optionalStatSheet.get());
    }

    @Operation(summary = "Create a new synthesized StatValue for the StatSheet.")
    @PostMapping("/worlds/{worldId}/sheet/{sheetId}/synthesized-stat")
    public StatValueSynthesizedDto addSynthesizedStatToSheet(
            @PathVariable int worldId,
            @PathVariable int sheetId,
            @RequestBody AddSynthesizedStatRequest request
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);
        if (optionalWorld.isEmpty())
            throw new NotFoundException(worldId);
        World world = optionalWorld.get();

        Optional<StatSheet> optionalStatSheet = statSheetService.getStatSheetRepository().findById(sheetId);
        if (optionalStatSheet.isEmpty())
            throw new NotFoundException(sheetId);
        StatSheet sheet = optionalStatSheet.get();

        StatValueSynthesized value = statSheetService.addSynthesizedStatValueToSheet(world, sheet, request.getName(), request.getNameShort(), request.getUnit(), request.getFormula());
        return StatValueSynthesizedDto.fromStatValueSynthesized(value);
    }

    @Operation(summary = "Create a new constant StatValue for the StatSheet.")
    @PostMapping("/worlds/{worldId}/sheet/{sheetId}/constant-stat")
    public StatValueDto addConstantStatToSheet(
            @PathVariable int worldId,
            @PathVariable int sheetId,
            @RequestBody AddConstantStatRequest request
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);
        if (optionalWorld.isEmpty())
            throw new NotFoundException(worldId);
        World world = optionalWorld.get();

        Optional<StatSheet> optionalStatSheet = statSheetService.getStatSheetRepository().findById(sheetId);
        if (optionalStatSheet.isEmpty())
            throw new NotFoundException(sheetId);
        StatSheet sheet = optionalStatSheet.get();

        StatValueConstant value = statSheetService.addConstantStatValueToSheet(world, sheet, request.getName(), request.getNameShort(), request.getUnit(), request.getInitialValue());
        return StatValueDto.fromStatValueConstant(value);
    }

    @Operation(summary = "Create a new Statsheet to the system.")
    @PostMapping("/worlds/{worldId}")
    public StatSheetDto add(
            @PathVariable int worldId,
            @RequestBody AddNamedResourceRequest request
    ) {
        Optional<World> optionalWorld = worldService.find(worldId);
        if (optionalWorld.isEmpty())
            throw new NotFoundException(worldId);
        World world = optionalWorld.get();

        StatSheet sheetNew = statSheetService.add(world, request.getName());
        return StatSheetDto.fromStatSheet(sheetNew);
    }
}

