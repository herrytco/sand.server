package systems.nope.discord.event.statistics;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.constants.EmoteConstants;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.RollUtil;

public class UserAverageEvent extends DiceEvent {
    private final float avg;

    public UserAverageEvent(MessageReceivedEvent event) {
        super(event);
        this.avg = RollUtil.getUserAverage(event.getMember());
    }

    public float getAvg() {
        return avg;
    }

    @Override
    public String toString() {
        if (avg != -1)
            return String.format("%s' average: %s %.2f", getAuthorName(), EmoteConstants.emoteRainbowDice, avg);
        else
            return String.format("%s did not do any rolling action so far, I am terribly sorry.", getAuthorName());
    }
}
