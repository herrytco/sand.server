package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.dto.InvokeActionDto;
import systems.nope.worldseed.exception.DataMissmatchException;
import systems.nope.worldseed.exception.ImpossibleException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.Action;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.SheetNode;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstance;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.repository.ActionRepository;
import systems.nope.worldseed.util.expression.ExpressionUtil;
import systems.nope.worldseed.util.expression.Symbol;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ActionService {
    private final ActionRepository actionRepository;
    private final PersonService personService;

    public ActionService(ActionRepository actionRepository, PersonService personService) {
        this.actionRepository = actionRepository;
        this.personService = personService;
    }

    public List<Action> allForItem(Item item) {
        return actionRepository.findAllByItem(item);
    }

    public Action add(Item item, World world, String name, String description, String formula, String message) {
        Action actionNew = new Action();

        actionNew.setWorld(world);
        actionNew.setItem(item);
        actionNew.setName(name);
        actionNew.setDescription(description);
        actionNew.setFormula(formula);
        actionNew.setInvokeMessage(message);

        actionRepository.save(actionNew);
        return actionNew;
    }

    public Action update(Action action, String name, String description, String formula, String message) {
        action.setName(name);
        action.setDescription(description);
        action.setFormula(formula);
        action.setInvokeMessage(message);

        actionRepository.save(action);
        return action;
    }

    public Action get(Integer id) {
        Optional<Action> optionalAction = find(id);

        if (optionalAction.isEmpty())
            throw new NotFoundException(id);

        return optionalAction.get();
    }

    public Optional<Action> find(Integer id) {
        return actionRepository.findById(id);
    }

    public InvokeActionDto invokeAction(Person person, Action action) throws DataMissmatchException {
        Item item = action.getItem();

        boolean found = false;

        for (Item personItem : person.getItems()) {
            if (personItem.getId().equals(item.getId())) {
                found = true;
                break;
            }
        }

        if(!found)
            throw new DataMissmatchException("Item does not belong to Person!");

        List<StatSheet> inputStatSheets = new LinkedList<>(
                person.getStatSheets()
        );

        String formula = action.getFormula();

        for (Symbol symbol : item.getStatValueInstances())
            formula = ExpressionUtil.groundFormula(
                    formula,
                    symbol
            );

        List<SheetNode> forest = personService.constructSheetForest(inputStatSheets);

        for (SheetNode tree : forest) {
            List<SheetNode> workingSet = new LinkedList<SheetNode>();
            workingSet.add(tree);

            while (workingSet.size() > 0) {
                SheetNode workingNode = workingSet.remove(0);

                for (StatValue statValue : workingNode.getSheet().getStatValues())
                    for (StatValuePersonInstance stat : person.getStatValues())
                        if (stat.getStatValue().getId() == statValue.getId())
                            formula = ExpressionUtil.groundFormula(formula, stat);

                workingSet.addAll(workingNode.getChildren());
            }
        }

        String message = action.getInvokeMessage();
        Integer result = (int) ExpressionUtil.parseExpression(formula);

        message = message.replaceAll("\\$USER", person.getName());
        message = message.replaceAll("\\$RESULT", String.valueOf(result));
        message = message.replaceAll("\\$FORMULA", formula);
        message = message.replaceAll("\\$ITEM", item.getName());
        message = message.replaceAll("\\$ACTION", action.getName());

        return InvokeActionDto.fromAction(
                action,
                result,
                message
        );
    }
}
