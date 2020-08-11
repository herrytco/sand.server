package systems.nope.discord.eventlistener.dice.person.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.event.DiceEvent;
import systems.nope.discord.eventlistener.dice.person.LinkUtils;

public class UnlinkEvent extends DiceEvent {

    private String message;

    public UnlinkEvent(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void handle() {
        if(LinkUtils.unlinkMember(getAuthor())) {
            message = "Link removed. You can now link to a new character sir.";
            LinkUtils.revertPersonNicknamingFromMember(getAuthor());
        }
        else
            message = "You are not linked to a character. Are you REALLY sure you linked before?";

        super.handle();
    }

    @Override
    public String toString() {
        return message;
    }
}
