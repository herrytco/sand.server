package systems.nope.worldseed.controller.stat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.dto.StatValueInstanceDto;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceConstant;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceSynthesized;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.exception.NotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/stat-value-instances")
public class StatValueInstanceController {

    private final StatSheetService statSheetService;

    public StatValueInstanceController(StatSheetService statSheetService) {
        this.statSheetService = statSheetService;
    }

    @GetMapping("/id/{id}")
    public StatValueInstanceDto one(
            @PathVariable int id
    ) {
        Optional<StatValueInstanceConstant> optionalStatValueInstanceConstant = statSheetService.findStatValueInstanceConstantById(id);

        if(optionalStatValueInstanceConstant.isPresent())
            return StatValueInstanceDto.fromInstance(optionalStatValueInstanceConstant.get());

        Optional<StatValueInstanceSynthesized> optionalStatValueInstanceSynthesized = statSheetService.findStatValueInstanceSynthesizedById(id);

        if(optionalStatValueInstanceSynthesized.isPresent()) {
            StatValueInstanceSynthesized stat = optionalStatValueInstanceSynthesized.get();
            statSheetService.enrichStatInstance(stat);

            return StatValueInstanceDto.fromInstance(stat);
        }

        throw new NotFoundException(id);
    }
}
