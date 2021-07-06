package systems.nope.discord.event.person;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.model.person.Action;
import systems.nope.discord.model.person.Person;
import systems.nope.discord.service.ActionService;
import systems.nope.discord.service.PersonService;
import systems.nope.discord.util.LinkUtils;

import java.io.IOException;
import java.util.Optional;

public class ActionEvent extends DiceEvent {

    public static ActionEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 2)
            return new ActionEvent(event, Integer.parseInt(command[1]));

        throw new ParseException("Sir, please use the command like !link <API-KEY>. The key can usually be found in your character sheet.");
    }

    private final String message;


    public ActionEvent(MessageReceivedEvent event, Integer actionId) {
        super(event);

        Optional<Person> optionalPerson = LinkUtils.getPersonForMember(event.getMember());

        if (optionalPerson.isEmpty()) {
            message = "Sir, you have to link before you can invoke an action ...";
            return;
        }

        Action invokedAction;
        try {
            invokedAction = ActionService.getActionForId(actionId);
        } catch (IOException e) {
            message = "Sir, the action you tried to invoke does definetly not exist. So stop pretending that it exists.";
            return;
        }

        if (invokedAction == null) {
            message = "No action found sir.";
            return;
        }

        Optional<String> actionMessage;

        try {
            actionMessage = ActionService.invokeAction(optionalPerson.get(), invokedAction);
        } catch (IOException e) {
            message = "Sir, the action you tried to invoke does definetly not exist. So stop pretending that it exists.";
            return;
        }

        if (actionMessage.isEmpty()) {
            message = "Sir, the action in question is not accessible for you or does not exist at all. Try again ... or don't for what I care.";
            return;
        }

        message = actionMessage.get();
    }


    @Override
    public String toString() {
        return message;
    }
}
