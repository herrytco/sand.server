package systems.nope.discord.event.statistics;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.RollUtil;

public class UserAverageResetEvent extends DiceEvent {

    public UserAverageResetEvent(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void handle() {
        RollUtil.resetAvgOfMember(getAuthor());
        super.handle();
    }

    @Override
    public String toString() {
        return String.format("I forgot everything what %s rolled.", getAuthorName());
    }
}
