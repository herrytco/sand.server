package systems.nope.worldseed.stat.sheet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.util.exceptions.NotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/stat-values")
public class StatValueController {

    private final StatSheetService statSheetService;

    public StatValueController(StatSheetService statSheetService) {
        this.statSheetService = statSheetService;
    }

    @GetMapping("/id/{id}")
    OutStatValue one(
            @PathVariable int id
    ) {
        Optional<StatValueConstant> statValueConstantOptional = statSheetService.findStatValueConstantById(id);

        if (statValueConstantOptional.isPresent())
            return OutStatValue.fromStatValueConstant(statValueConstantOptional.get());

        Optional<StatValueSynthesized> optionalStatValueSynthesized = statSheetService.findStatValueSynthesizedById(id);

        if (optionalStatValueSynthesized.isPresent())
            return OutStatValueSynthesized.fromStatValueSynthesized(optionalStatValueSynthesized.get());

        throw new NotFoundException(id);
    }
}
