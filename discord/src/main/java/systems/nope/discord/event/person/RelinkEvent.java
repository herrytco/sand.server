package systems.nope.discord.event.person;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.LinkUtils;
import systems.nope.discord.model.person.Person;

import java.io.IOException;

public class RelinkEvent extends DiceEvent {
    Person linkedPerson;

    public RelinkEvent(MessageReceivedEvent event) {
        super(event);

        try {
           linkedPerson = LinkUtils.relinkPerson(getAuthor());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        if (linkedPerson == null)
            return "I don't remember any Links from you sir, I am terribly sorry.";

        return String.format("Reestablished Link to '%s'", linkedPerson.getName());
    }
}
