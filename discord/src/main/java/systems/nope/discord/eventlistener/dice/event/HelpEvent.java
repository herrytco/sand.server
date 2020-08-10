package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.ServerConstants;

public class HelpEvent extends DiceEvent {
    public HelpEvent(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public String toString() {
        return ServerConstants.help;
    }
}
