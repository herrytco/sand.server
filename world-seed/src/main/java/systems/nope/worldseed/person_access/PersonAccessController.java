package systems.nope.worldseed.person_access;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
