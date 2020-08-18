package systems.nope.worldseed.stat.sheet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.stat.model.StatValueInstanceConstant;
import systems.nope.worldseed.stat.model.StatValueInstanceSynthesized;
import systems.nope.worldseed.util.exceptions.NotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/stat-value-instances")
public class StatValueInstanceController {

    private final StatSheetService statSheetService;

    public StatValueInstanceController(StatSheetService statSheetService) {
        this.statSheetService = statSheetService;
    }

    @GetMapping("/id/{id}")
    public OutStatValueInstance one(
            @PathVariable int id
    ) {
        Optional<StatValueInstanceConstant> optionalStatValueInstanceConstant = statSheetService.findStatValueInstanceConstantById(id);

        if(optionalStatValueInstanceConstant.isPresent())
            return OutStatValueInstance.fromInstance(optionalStatValueInstanceConstant.get());

        Optional<StatValueInstanceSynthesized> optionalStatValueInstanceSynthesized = statSheetService.findStatValueInstanceSynthesizedById(id);

        if(optionalStatValueInstanceSynthesized.isPresent()) {
            StatValueInstanceSynthesized stat = optionalStatValueInstanceSynthesized.get();
            statSheetService.enrichStatInstance(stat);

            return OutStatValueInstance.fromInstance(stat);
        }

        throw new NotFoundException(id);
    }
}
