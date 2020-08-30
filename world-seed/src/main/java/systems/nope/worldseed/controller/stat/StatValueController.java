package systems.nope.worldseed.controller.stat;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.StatValueConstantDto;
import systems.nope.worldseed.dto.StatValueDto;
import systems.nope.worldseed.dto.StatValueSynthesizedDto;
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

        for(StatValue value : statSheetService.get(statSheetId).getStatValues()) {
            if (value instanceof StatValueConstant)
                values.add(StatValueConstantDto.fromStatValueConstant((StatValueConstant) value));
            else if (value instanceof StatValueSynthesized)
                values.add(StatValueSynthesizedDto.fromStatValueSynthesized((StatValueSynthesized) value));
        }

        return values;
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
        Optional<StatValueConstant> statValueConstantOptional = statSheetService.findStatValueConstantById(id);

        if (statValueConstantOptional.isPresent())
            return StatValueConstantDto.fromStatValueConstant(statValueConstantOptional.get());

        Optional<StatValueSynthesized> optionalStatValueSynthesized = statSheetService.findStatValueSynthesizedById(id);

        if (optionalStatValueSynthesized.isPresent())
            return StatValueSynthesizedDto.fromStatValueSynthesized(optionalStatValueSynthesized.get());

        throw new NotFoundException(id);
    }
}
