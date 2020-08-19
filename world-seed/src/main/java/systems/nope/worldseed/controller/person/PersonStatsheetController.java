package systems.nope.worldseed.controller.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.service.PersonService;
import systems.nope.worldseed.dto.request.AddPersonStatsheetRequest;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceConstant;
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

    @PutMapping("/id/{instanceId}/constant-stat")
    public ResponseEntity<?> updateStatSheetMapping(
            @PathVariable int instanceId,
            @RequestBody UpdateConstantStatValueIntanceRequest request
    ) {
        logger.info(request.toString());

        Optional<StatValueInstanceConstant> instance = statSheetService.getStatValueInstanceConstantRepository().findById(instanceId);

        if (instance.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Mapping with ID '%d' not found.", instanceId));

        statSheetService.updateConstantStatInstance(instance.get(), request.getValueNew());

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addStatsheetMapping(
            @RequestBody AddPersonStatsheetRequest request
    ) {
        logger.info(request.toString());
        Optional<Person> optionalPerson = personService.getPersonRepository().findById(request.getPersonId());

        if (optionalPerson.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Character with ID '%d' not found.", request.getPersonId()));

        Optional<StatSheet> optionalStatSheet = statSheetService.getStatSheetRepository().findById(request.getStatsheetId());

        if (optionalStatSheet.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Character with ID '%d' not found.", request.getStatsheetId()));

        personService.addStatSheetToPerson(optionalPerson.get(), optionalStatSheet.get());
        return ResponseEntity.ok().build();
    }


}
