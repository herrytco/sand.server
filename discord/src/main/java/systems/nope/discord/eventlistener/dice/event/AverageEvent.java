package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceUtils;
import systems.nope.discord.eventlistener.dice.ServerConstants;

public class AverageEvent extends DiceEvent {
    private final float avg;
    private final AverageScope scope;

    public AverageEvent(MessageReceivedEvent event, AverageScope scope) {
        super(event);
        this.scope = scope;

        switch (scope) {
            case User:
                this.avg = DiceUtils.avgOfMember(event.getMember());
                break;
            default:
            case Server:
                this.avg = DiceUtils.avgOfServer();
                break;
        }
    }

    public float getAvg() {
        return avg;
    }

    @Override
    public String toString() {
        switch (scope) {
            case User:
                if (avg != -1)
                    return String.format("%s' average: %s %.2f", getAuthorName(), ServerConstants.emoteRainbowDice, avg);
                else
                    return String.format("%s did not do any rolling action so far, I am terribly sorry.", getAuthorName());

            default:
            case Server:
                if (avg > 0)
                    return String.format("Server average: %s %.2f", ServerConstants.emoteRainbowDice, avg);
                else
                    return "Boys, if you want some averages you have to ROLL to get empirical data to average!";
        }
    }
}
