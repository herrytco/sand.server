package systems.nope.worldseed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.model.Skill;
import systems.nope.worldseed.repository.SkillRepository;

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
