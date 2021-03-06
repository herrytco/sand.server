package systems.nope.worldseed.controller.person;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.request.stat.AddResourceToStatSheetRequest;
import systems.nope.worldseed.dto.request.stat.UpdateConstantStatValueInstanceRequest;
import systems.nope.worldseed.dto.request.stat.UpdateSynthesizedStatValueInstanceRequest;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.service.StatValueInstanceService;
import systems.nope.worldseed.service.person.PersonService;

@RestController
@RequestMapping("/stat-sheet-mapping")
public class PersonStatsheetController {

    private final PersonService personService;
    private final StatSheetService statSheetService;
    private final StatValueInstanceService statValueInstanceService;

    private final Logger logger = LoggerFactory.getLogger(PersonStatsheetController.class);

    public PersonStatsheetController(PersonService personService, StatSheetService statSheetService,
                                     StatValueInstanceService statValueInstanceService) {
        this.personService = personService;
        this.statSheetService = statSheetService;
        this.statValueInstanceService = statValueInstanceService;
    }

    @Operation(summary = "Update a single StatValueInstance identified by its id to a new value.")
    @PutMapping("/id/{instanceId}/constant-stat")
    public void updateConstantStatSheetMapping(
            @PathVariable int instanceId,
            @RequestBody UpdateConstantStatValueInstanceRequest request
    ) {
        logger.info(request.toString());

        statValueInstanceService.update(
                statValueInstanceService.getConstant(instanceId),
                request.getValueNew(),
                request.getModifierNew()
        );
    }

    @Operation(summary = "Update a single StatValueInstance identified by its id to a new value.")
    @PutMapping("/id/{instanceId}/synthesized-stat")
    public void updateSynthesizedStatSheetMapping(
            @PathVariable int instanceId,
            @RequestBody UpdateSynthesizedStatValueInstanceRequest request
    ) {
        logger.info(request.toString());

        statValueInstanceService.update(
                statValueInstanceService.getSynthesized(instanceId),
                request.getModifierNew()
        );
    }

    @Operation(summary = "Add a StatSheet identified by its id to a Character, identified by its id.")
    @PostMapping
    public void addStatsheetMapping(
            @RequestBody AddResourceToStatSheetRequest request
    ) {
        logger.info(request.toString());

        personService.addStatSheetToPerson(
                personService.get(request.getTargetId()),
                statSheetService.get(request.getStatsheetId())
        );
    }


}
