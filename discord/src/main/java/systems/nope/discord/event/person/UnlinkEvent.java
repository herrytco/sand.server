package systems.nope.discord.event.person;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.LinkUtils;

import java.io.IOException;

public class UnlinkEvent extends DiceEvent {

    private String message;

    public UnlinkEvent(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void handle() {
        try {
            message = "Link removed. You can now link to a new character sir.";
            LinkUtils.revertPersonNicknamingFromMember(getAuthor());
        } catch (IOException e) {
            message = "Something bad happened, are you now linked forever?";
        }

        super.handle();
    }

    @Override
    public String toString() {
        return message;
    }
}
