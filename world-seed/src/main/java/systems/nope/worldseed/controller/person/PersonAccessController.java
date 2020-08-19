package systems.nope.worldseed.controller.person;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.model.PersonAccess;
import systems.nope.worldseed.repository.PersonAccessRepository;

import java.util.List;

@RestController
@RequestMapping("person-access")
public class PersonAccessController {
    private final PersonAccessRepository personAccessRepository;

    public PersonAccessController(PersonAccessRepository personAccessRepository) {
        this.personAccessRepository = personAccessRepository;
    }

    @GetMapping
    public List<PersonAccess> all() {
        return personAccessRepository.findAll();
    }
}
