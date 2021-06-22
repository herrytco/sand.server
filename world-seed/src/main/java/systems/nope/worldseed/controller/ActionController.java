package systems.nope.worldseed.controller;

import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.dto.ActionDto;
import systems.nope.worldseed.dto.InvokeActionDto;
import systems.nope.worldseed.dto.request.AddActionRequest;
import systems.nope.worldseed.repository.ActionRepository;
import systems.nope.worldseed.service.ActionService;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.service.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/actions")
public class ActionController {
    private final ActionRepository actionRepository;

    private final ActionService actionService;
    private final PersonService personService;
    private final ItemService itemService;

    public ActionController(ActionRepository actionRepository, ActionService actionService, PersonService personService, ItemService itemService) {
        this.actionRepository = actionRepository;
        this.actionService = actionService;
        this.personService = personService;
        this.itemService = itemService;
    }

    @GetMapping("/{actionId}/items/{itemId}/persons/{personId}")
    public InvokeActionDto invokeAction(
            @PathVariable Integer actionId,
            @PathVariable Integer itemId,
            @PathVariable Integer personId
    ) {
        return actionService.invokeAction(
                personService.get(personId),
                itemService.get(itemId),
                actionService.get(actionId)
        );
    }

    @GetMapping
    public List<ActionDto> all() {
        return actionRepository.findAll().stream().map(ActionDto::fromAction).collect(Collectors.toList());
    }
}
