package systems.nope.discord.event.statistics;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.RollUtil;

public class ServerAverageResetEvent extends DiceEvent {

    public ServerAverageResetEvent(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void handle() {
        RollUtil.resetAvgOfSever();
        super.handle();
    }

    @Override
    public String toString() {
        return "I forgot everything each of you did ... Who are you guys again?";
    }
}
