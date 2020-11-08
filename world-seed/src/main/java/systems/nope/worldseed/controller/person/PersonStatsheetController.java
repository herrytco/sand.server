package systems.nope.worldseed.controller.person;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.service.PersonService;
import systems.nope.worldseed.dto.request.AddPersonStatsheetRequest;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstanceConstant;
import systems.nope.worldseed.dto.request.UpdateConstantStatValueIntanceRequest;
import systems.nope.worldseed.service.StatSheetService;

import java.util.Optional;

@RestController
@RequestMapping("/stat-sheet-mapping")
public class PersonStatsheetController {

    private final PersonService personService;
    private final StatSheetService statSheetService;

    private final Logger logger = LoggerFactory.getLogger(PersonStatsheetController.class);

    public PersonStatsheetController(PersonService personService, StatSheetService statSheetService) {
        this.personService = personService;
        this.statSheetService = statSheetService;
    }

    @Operation(summary = "Update a single StatValueInstance identified by its id to a new value.")
    @PutMapping("/id/{instanceId}/constant-stat")
    public void updateStatSheetMapping(
            @PathVariable int instanceId,
            @RequestBody UpdateConstantStatValueIntanceRequest request
    ) {
        logger.info(request.toString());

        Optional<StatValuePersonInstanceConstant> instance = statSheetService.getStatValueInstanceConstantRepository().findById(instanceId);

        if (instance.isEmpty())
            throw new NotFoundException(instanceId);

        statSheetService.updateConstantStatInstance(instance.get(), request.getValueNew());
    }

    @Operation(summary = "Add a StatSheet identified by its id to a Character, identified by its id.")
    @PostMapping
    public void addStatsheetMapping(
            @RequestBody AddPersonStatsheetRequest request
    ) {
        logger.info(request.toString());
        Optional<Person> optionalPerson = personService.getPersonRepository().findById(request.getPersonId());

        if (optionalPerson.isEmpty())
            throw new NotFoundException(request.getPersonId());

        Optional<StatSheet> optionalStatSheet = statSheetService.getStatSheetRepository().findById(request.getStatsheetId());

        if (optionalStatSheet.isEmpty())
            throw new NotFoundException(request.getStatsheetId());

        personService.addStatSheetToPerson(optionalPerson.get(), optionalStatSheet.get());
    }


}
