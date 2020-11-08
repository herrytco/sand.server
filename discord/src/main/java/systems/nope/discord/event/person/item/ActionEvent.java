package systems.nope.discord.event.person.item;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.model.person.Person;
import systems.nope.discord.util.ActionUtils;
import systems.nope.discord.util.LinkUtils;

import java.io.IOException;
import java.util.Optional;

public class ActionEvent extends DiceEvent {
    private final int itemId;

    private final int actionId;

    private String message = "";

    public ActionEvent(MessageReceivedEvent event, int itemId, int actionId) {
        super(event);
        this.itemId = itemId;
        this.actionId = actionId;

        Optional<Person> optionalPerson = LinkUtils.getPersonForMember(event.getMember());

        if (optionalPerson.isEmpty()) {
            message = "Only people linked to a character can invoke actions. But i am sure you already knew that.";
            return;
        }

        Person person = optionalPerson.get();

        try {
            message = ActionUtils.invokeAction(itemId, actionId, person);
        } catch (IOException e) {
            message = "The queen is currently not responding to my messages. Women...";
        }
    }

    public int getItemId() {
        return itemId;
    }

    public int getActionId() {
        return actionId;
    }

    @Override
    public String toString() {
        return message;
    }
}
