package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceResult;
import systems.nope.discord.eventlistener.dice.DiceUtils;
import systems.nope.discord.eventlistener.dice.Target;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OtherRollEvent extends DiceEvent {

    private final List<Target> targetList;
    private final Map<Target, List<DiceResult>> results = new HashMap<>();

    public OtherRollEvent(MessageReceivedEvent event, List<Target> targetList) {
        super(event);
        this.targetList = targetList;

        for (Target t : targetList) {
            List<DiceResult> tResults = new LinkedList<>();

            for (int i = 0; i < t.getNumberOfRolls(); i++)
                tResults.add(DiceUtils.rollOnce(getAuthor()));

            results.put(t, tResults);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Target t : targetList) {
            sb.append(String.format("%s rolls %d dice:\n", t.getName(), t.getNumberOfRolls()));

            for (DiceResult r : results.get(t))
                sb.append(DiceUtils.getCalculation(r)).append("\n");
        }

        return sb.toString();
    }
}
