package systems.nope.worldseed.skill;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {

    public SkillController(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    private final SkillRepository skillRepository;

    @GetMapping
    private List<Skill> all() {
        return skillRepository.findAll();
    }
}
