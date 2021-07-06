package systems.nope.worldseed.controller.stat;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.stat.person.StatValuePersonInstanceDto;
import systems.nope.worldseed.service.person.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stat-value-instances")
public class StatValueInstanceController {

    private final PersonService personService;

    public StatValueInstanceController(PersonService personService) {
        this.personService = personService;
    }

    @Operation(summary = "Get a set of StatValueInstances all belonging to the same person.")
    @GetMapping("/person/id/{personId}")
    public List<StatValuePersonInstanceDto> forPerson(
            @PathVariable int personId
    ) {
        return personService.get(personId).getStatValues().stream().map(StatValuePersonInstanceDto::fromInstance).collect(Collectors.toList());
    }
}
