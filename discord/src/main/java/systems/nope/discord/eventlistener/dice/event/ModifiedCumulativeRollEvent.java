package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceResult;
import systems.nope.discord.eventlistener.dice.DiceUtils;
import systems.nope.discord.util.StringUtil;

public class ModifiedCumulativeRollEvent extends CumulativeRollEvent {
    private final int modifier;

    public ModifiedCumulativeRollEvent(MessageReceivedEvent event, int numberOfRolls, int modifier) {
        super(event, numberOfRolls);
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s rolls %d culative dice with a modifier of %d:", getAuthorName(), numberOfRolls, modifier)).append("\n");

        int resultCumulative = 0;

        for (int i = 0, resultsSize = results.size(); i < resultsSize; i++) {
            DiceResult r = results.get(i);

            sb.append(
                    String.format(
                            "%s's %s dice: %s %s %d",
                            getAuthorName(),
                            StringUtil.integerToOrderedString(i + 1),
                            DiceUtils.getCalculation(r),
                            modifier > 0 ? "+" : "-",
                            Math.abs(modifier)
                    )
            ).append("\n");

            resultCumulative += (r.getEffectiveResult() + modifier);
        }

        sb.append(String.format("The total sum is ... *furiously typing on calculator* ... %d!", resultCumulative));

        return sb.toString();
    }
}
