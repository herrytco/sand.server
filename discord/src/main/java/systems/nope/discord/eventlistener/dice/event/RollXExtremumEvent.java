package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceResult;
import systems.nope.discord.eventlistener.dice.DiceUtils;

public class RollXExtremumEvent extends RollXEvent {
    private final boolean isMaximum;
    private DiceResult result;

    public RollXExtremumEvent(MessageReceivedEvent event, int numberOfRolls, boolean isMaximum) {
        super(event, numberOfRolls);
        this.isMaximum = isMaximum;

        for (DiceResult ri : results) {
            if (result == null) {
                result = ri;
                continue;
            }

            if (isMaximum) {
                if (ri.getResult() > result.getResult())
                    result = ri;
            } else {
                if (ri.getResult() < result.getResult())
                    result = ri;
            }
        }
    }

    public boolean isMaximum() {
        return isMaximum;
    }

    public DiceResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        StringBuilder all = new StringBuilder("(");
        for (int i = 0; i < results.size(); i++) {
            all.append(DiceUtils.getCalculation(results.get(i)));

            if (i < results.size() - 1)
                all.append(", ");
        }
        all.append(")");

        return String.format(
                "%s rolled %d dice, the %s result is %s!\nAll rolls were:%s",
                getAuthorName(),
                numberOfRolls,
                isMaximum ? "best" : "worst",
                DiceUtils.getCalculation(result),
                all.toString()
        );
    }
}
