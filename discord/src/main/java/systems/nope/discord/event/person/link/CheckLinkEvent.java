package systems.nope.discord.event.person.link;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.LinkUtils;
import systems.nope.discord.model.person.Person;

import java.util.Optional;

public class CheckLinkEvent extends DiceEvent {
    private final String message;

    public CheckLinkEvent(MessageReceivedEvent event) {
        super(event);

        Optional<Person> linkedPerson =LinkUtils.getPersonForMember(getAuthor());

        if (linkedPerson.isPresent()) {
            this.message = String.format("%s, you are currently linked to the character '%s'. A good choice if I can say so.",
                    getAuthorName(),
                    linkedPerson.get().getName());
        } else
            this.message = "You are not linked to a character. Are you REALLY sure you linked before?";
    }

    @Override
    public String toString() {
        return message;
    }
}
