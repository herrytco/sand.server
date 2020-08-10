package systems.nope.discord.eventlistener.dice.person.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.event.DiceEvent;
import systems.nope.discord.eventlistener.dice.person.LinkUtils;

public class CheckLinkEvent extends DiceEvent {
    private final String message;

    public CheckLinkEvent(MessageReceivedEvent event) {
        super(event);

        if (LinkUtils.isMemberLinked(getAuthor()))
            this.message = String.format("%s, you are currently linked to the character '%s'. A good choice if I can say so.",
                    getAuthorName(),
                    LinkUtils.getPersonForMember(getAuthor()).getName());
        else
            this.message = "You are not linked to a character. Are you REALLY sure you linked before?";
    }

    @Override
    public String toString() {
        return message;
    }
}
