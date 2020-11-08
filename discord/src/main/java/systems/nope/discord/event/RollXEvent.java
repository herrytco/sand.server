package systems.nope.discord.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.DiceUtils;

import java.util.LinkedList;
import java.util.List;

public class RollXEvent extends DiceEvent{
    final List<DiceResult> results = new LinkedList<>();
    final int numberOfRolls;

    public RollXEvent(MessageReceivedEvent event, int numberOfRolls) {
        super(event);
        this.numberOfRolls = numberOfRolls;

        for (int i = 0; i < numberOfRolls; i++)
            results.add(DiceUtils.rollOnce(getAuthor()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s rolls %d dice:\n", getAuthorName(), numberOfRolls));

        for(DiceResult result : results) {
            sb.append(DiceUtils.diceResultToString(getAuthorName(), result)).append("\n");
        }

        return sb.toString();
    }
}
