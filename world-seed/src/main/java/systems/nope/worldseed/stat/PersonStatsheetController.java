package systems.nope.worldseed.stat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.person.Person;
import systems.nope.worldseed.person.PersonService;
import systems.nope.worldseed.person.requests.AddPersonStatsheetRequest;
import systems.nope.worldseed.stat.sheet.StatSheet;
import systems.nope.worldseed.stat.model.StatValueInstanceConstant;
import systems.nope.worldseed.stat.requests.UpdateConstantStatValueIntanceRequest;
import systems.nope.worldseed.stat.sheet.StatSheetService;

import java.util.Optional;

@RestController
@RequestMapping("/stat-sheet-mapping")
public class PersonStatsheetController {

    private final PersonService personService;
    private final StatSheetService statSheetService;

    public PersonStatsheetController(PersonService personService, StatSheetService statSheetService) {
        this.personService = personService;
        this.statSheetService = statSheetService;
    }

    @PutMapping("/id/{instanceId}/constant-stat")
    public ResponseEntity<?> updateStatSheetMapping(
            @PathVariable int instanceId,
            @RequestBody UpdateConstantStatValueIntanceRequest request
    ) {
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
