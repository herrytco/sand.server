package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceResult;
import systems.nope.discord.eventlistener.dice.DiceUtils;

public class ModifiedRollXEvent extends RollXEvent {
    private final int modifier;

    public ModifiedRollXEvent(MessageReceivedEvent event, int numberOfRolls, int modifier) {
        super(event, numberOfRolls);
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s rolls %d dice:\n", getAuthorName(), numberOfRolls));

        for (DiceResult result : results) {
            sb.append(DiceUtils.diceResultToString(getAuthorName(), result))
                    .append(DiceUtils.getModifiedCalculation(result, modifier))
                    .append("\n");
        }

        return sb.toString();
    }
}
