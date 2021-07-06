package systems.nope.worldseed.controller.stat;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.stat.person.StatValuePersonInstanceDto;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstanceConstant;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstanceSynthesized;
import systems.nope.worldseed.service.person.PersonService;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/stat-value-instances")
public class StatValueInstanceController {

    private final PersonService personService;
    private final StatSheetService statSheetService;

    public StatValueInstanceController(PersonService personService, StatSheetService statSheetService) {
        this.personService = personService;
        this.statSheetService = statSheetService;
    }

    @Operation(summary = "Get a set of StatValueInstances all belonging to the same person.")
    @GetMapping("/person/id/{personId}")
    public List<StatValuePersonInstanceDto> forPerson(
            @PathVariable int personId
    ) {
        return personService.get(personId).getStatValues().stream().map(StatValuePersonInstanceDto::fromInstance).collect(Collectors.toList());
    }

    @Operation(summary = "Get a set of StatValueInstances identified by their ids.")
    @GetMapping
    public List<StatValuePersonInstanceDto> multiple(
            @RequestParam(name = "id") Integer[] ids
    ) {
        return Stream.of(ids).map(this::one).collect(Collectors.toList());
    }

    @Operation(summary = "Get one StatValueInstance identified by its id.")
    @GetMapping("/id/{id}")
    public StatValuePersonInstanceDto one(
            @PathVariable int id
    ) {
        Optional<StatValuePersonInstanceConstant> optionalStatValueInstanceConstant = statSheetService.findStatValueInstanceConstantById(id);

        if (optionalStatValueInstanceConstant.isPresent())
            return StatValuePersonInstanceDto.fromInstance(optionalStatValueInstanceConstant.get());

        Optional<StatValuePersonInstanceSynthesized> optionalStatValueInstanceSynthesized = statSheetService.findStatValueInstanceSynthesizedById(id);

        if (optionalStatValueInstanceSynthesized.isPresent()) {
            StatValuePersonInstanceSynthesized stat = optionalStatValueInstanceSynthesized.get();
            personService.enrichStatInstance(stat);

            return StatValuePersonInstanceDto.fromInstance(stat);
        }

        throw new NotFoundException(id);
    }
}
