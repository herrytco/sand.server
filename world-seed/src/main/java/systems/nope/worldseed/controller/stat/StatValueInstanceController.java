package systems.nope.worldseed.controller.stat;

import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.StatValueInstanceDto;
import systems.nope.worldseed.dto.request.MultiIdRequest;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceConstant;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceSynthesized;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stat-value-instances")
public class StatValueInstanceController {

    private final StatSheetService statSheetService;

    public StatValueInstanceController(StatSheetService statSheetService) {
        this.statSheetService = statSheetService;
    }

    @GetMapping
    public List<StatValueInstanceDto> multiple(
            @RequestBody MultiIdRequest request
    ) {
        return request.getIds().stream().map(this::one).collect(Collectors.toList());
    }

    @GetMapping("/id/{id}")
    public StatValueInstanceDto one(
            @PathVariable int id
    ) {
        Optional<StatValueInstanceConstant> optionalStatValueInstanceConstant = statSheetService.findStatValueInstanceConstantById(id);

        if (optionalStatValueInstanceConstant.isPresent())
            return StatValueInstanceDto.fromInstance(optionalStatValueInstanceConstant.get());

        Optional<StatValueInstanceSynthesized> optionalStatValueInstanceSynthesized = statSheetService.findStatValueInstanceSynthesizedById(id);

        if (optionalStatValueInstanceSynthesized.isPresent()) {
            StatValueInstanceSynthesized stat = optionalStatValueInstanceSynthesized.get();
            statSheetService.enrichStatInstance(stat);

            return StatValueInstanceDto.fromInstance(stat);
        }

        throw new NotFoundException(id);
    }
}
