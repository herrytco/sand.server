package systems.nope.discord.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.DiceUtils;
import systems.nope.discord.util.StringUtil;

public class CumulativeRollEvent extends RollXEvent {
    public CumulativeRollEvent(MessageReceivedEvent event, int numberOfRolls) {
        super(event, numberOfRolls);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s rolls %d culative dice:", getAuthorName(), numberOfRolls)).append("\n");

        int resultCumulative = 0;

        for (int i = 0, resultsSize = results.size(); i < resultsSize; i++) {
            DiceResult r = results.get(i);

            sb.append(
                    String.format(
                            "%s's %s dice: %s",
                            getAuthorName(),
                            StringUtil.integerToOrderedString(i + 1),
                            DiceUtils.getCalculation(r)
                    )
            ).append("\n");

            resultCumulative += r.getEffectiveResult();
        }

        sb.append(String.format("The total sum is ... *furiously typing on calculator* ... %d!", resultCumulative));

        return sb.toString();
    }


}
