package systems.nope.discord.event.statistics;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.constants.EmoteConstants;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.RollUtil;

public class ServerAverageEvent extends DiceEvent {
    private final float avg;

    public ServerAverageEvent(MessageReceivedEvent event) {
        super(event);
        this.avg = RollUtil.getServerAverage();
    }

    public float getAvg() {
        return avg;
    }

    @Override
    public String toString() {
        if (avg != -1)
            return String.format("Server average: %s %.2f", EmoteConstants.emoteRainbowDice, avg);
        else
            return "Boys, if you want some averages you have to ROLL to get empirical data to average!";
    }
}
