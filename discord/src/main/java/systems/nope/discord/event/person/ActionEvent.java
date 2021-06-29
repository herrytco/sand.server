package systems.nope.discord.event.person;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.model.person.Action;
import systems.nope.discord.service.ActionService;

import java.io.IOException;

public class ActionEvent extends DiceEvent {

    public static ActionEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 2)
            return new ActionEvent(event, Integer.parseInt(command[1]));

        throw new ParseException("Sir, please use the command like !link <API-KEY>. The key can usually be found in your character sheet.");
    }

    private final Integer actionId;
    private final Action invokedAction;
    private final String message;


    public ActionEvent(MessageReceivedEvent event, Integer actionId) {
        super(event);

        this.actionId = actionId;

        Action action = null;
        try {
            action = ActionService.getActionForId(actionId);
        } catch (IOException e) {
            message = "Sir, the action you tried to invoke does definetly not exist. So stop pretending that it exists.";
            invokedAction = null;
            return;
        }

        invokedAction = action;
        message = this.toString();
    }

    @Override
    public String toString() {
        return "ActionEvent{" +
                "actionId=" + actionId +
                '}';
    }
}
