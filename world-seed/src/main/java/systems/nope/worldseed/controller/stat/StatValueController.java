package systems.nope.worldseed.controller.stat;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.stat.StatValueConstantDto;
import systems.nope.worldseed.dto.stat.StatValueDto;
import systems.nope.worldseed.dto.stat.StatValueSynthesizedDto;
import systems.nope.worldseed.dto.request.stat.UpdateStatFormulaRequest;
import systems.nope.worldseed.dto.request.stat.UpdateStatInitialValueRequest;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.exception.NotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/stat-values")
public class StatValueController {

    private final StatSheetService statSheetService;

    public StatValueController(StatSheetService statSheetService) {
        this.statSheetService = statSheetService;
    }

    @Operation(summary = "Get a set of StatValues all belonging to the same StatSheet.")
    @GetMapping("/stat-sheet/id/{statSheetId}")
    public List<StatValueDto> forStatSheet(
            @PathVariable int statSheetId
    ) {
        List<StatValueDto> values = new LinkedList<>();

        for (StatValue value : statSheetService.get(statSheetId).getStatValues()) {
            if (value instanceof StatValueConstant)
                values.add(StatValueConstantDto.fromStatValueConstant((StatValueConstant) value));
            else if (value instanceof StatValueSynthesized)
                values.add(StatValueSynthesizedDto.fromStatValueSynthesized((StatValueSynthesized) value));
        }

        return values;
    }

    @DeleteMapping("/id/{statValueId}")
    public void deleteStatValue(
            @PathVariable int statValueId
    ) {
        statSheetService.deleteStatValue(statValueId);
    }

    @PutMapping("/id/{statValueId}/initial-value")
    public void updateConstantStatValue(
            @PathVariable int statValueId,
            @RequestBody UpdateStatInitialValueRequest request
    ) {
        statSheetService.updateStatValueContant(statValueId, request.getInitialValue());
    }

    @PutMapping("/id/{statValueId}/formula")
    public void updateSynthesizedStatValue(
            @PathVariable int statValueId,
            @RequestBody UpdateStatFormulaRequest request
    ) {
        statSheetService.updateStatValueSynthesized(statValueId, request.getFormula());
    }

    @Operation(summary = "Get a set of StatValues identified by their ids.")
    @GetMapping
    public List<StatValueDto> multiple(
            @RequestParam(name = "id") Integer[] ids
    ) {
        return Stream.of(ids).map(this::one).collect(Collectors.toList());
    }

    @Operation(summary = "Get one StatValue identified by its id.")
    @GetMapping("/id/{id}")
    public StatValueDto one(
            @PathVariable int id
    ) {
        Optional<StatValueConstant> statValueConstantOptional = statSheetService.findConstant(id);

        if (statValueConstantOptional.isPresent())
            return StatValueConstantDto.fromStatValueConstant(statValueConstantOptional.get());

        Optional<StatValueSynthesized> optionalStatValueSynthesized = statSheetService.findSynthesized(id);

        if (optionalStatValueSynthesized.isPresent())
            return StatValueSynthesizedDto.fromStatValueSynthesized(optionalStatValueSynthesized.get());

        throw new NotFoundException(id);
    }
}
