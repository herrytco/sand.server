package systems.nope.worldseed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.dto.ActionDto;
import systems.nope.worldseed.repository.ActionRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/actions")
public class ActionController {
    private final ActionRepository actionRepository;

    public ActionController(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @GetMapping
    public List<ActionDto> all() {
        return actionRepository.findAll().stream().map(ActionDto::fromAction).collect(Collectors.toList());
    }
}
