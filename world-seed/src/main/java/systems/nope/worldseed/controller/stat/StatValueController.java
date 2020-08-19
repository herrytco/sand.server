package systems.nope.worldseed.controller.stat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.dto.StatValueDto;
import systems.nope.worldseed.dto.StatValueSynthesizedDto;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.exception.NotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/stat-values")
public class StatValueController {

    private final StatSheetService statSheetService;

    public StatValueController(StatSheetService statSheetService) {
        this.statSheetService = statSheetService;
    }

    @GetMapping("/id/{id}")
    StatValueDto one(
            @PathVariable int id
    ) {
        Optional<StatValueConstant> statValueConstantOptional = statSheetService.findStatValueConstantById(id);

        if (statValueConstantOptional.isPresent())
            return StatValueDto.fromStatValueConstant(statValueConstantOptional.get());

        Optional<StatValueSynthesized> optionalStatValueSynthesized = statSheetService.findStatValueSynthesizedById(id);

        if (optionalStatValueSynthesized.isPresent())
            return StatValueSynthesizedDto.fromStatValueSynthesized(optionalStatValueSynthesized.get());

        throw new NotFoundException(id);
    }
}
