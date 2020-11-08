package systems.nope.discord.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.DiceUtils;

public class ModifiedRollXExtremumEvent extends RollXExtremumEvent {
    private final int modifier;

    public ModifiedRollXExtremumEvent(MessageReceivedEvent event, int numberOfRolls, boolean isMaximum, int modifier) {
        super(event, numberOfRolls, isMaximum);
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        StringBuilder all = new StringBuilder("(");
        for (int i = 0; i < results.size(); i++) {
            DiceResult result = results.get(i);
            all.append(DiceUtils.getModifiedCalculation(result, modifier));

            if (i < results.size() - 1)
                all.append(", ");
        }
        all.append(")");

        return String.format(
                "%s rolled %d dice, the %s result is %s!\nAll rolls were:%s",
                getAuthorName(),
                numberOfRolls,
                isMaximum() ? "best" : "worst",
                DiceUtils.getModifiedCalculation(getResult(), modifier),
                all.toString()
        );
    }
}
