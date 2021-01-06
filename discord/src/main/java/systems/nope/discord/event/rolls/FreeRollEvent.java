package systems.nope.discord.event.rolls;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.RngUtil;
import systems.nope.discord.util.RollUtil;

import java.util.LinkedList;
import java.util.List;

public class FreeRollEvent extends DiceEvent {

    public static FreeRollEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 1)
            throw new ParseException("Sir, please use the command like !rollf <DICE> <DICE> ... where <DICE> is a string like '1d20'");

        List<RollSpecification> specs = new LinkedList<>();

        for (int i = 1; i < command.length; i++)
            specs.add(RollSpecification.fromString(command[i]));

        return new FreeRollEvent(event, specs);
    }

    private final List<RollSpecification> specifications = new LinkedList<>();
    final List<DiceResult> results = new LinkedList<>();

    public FreeRollEvent(MessageReceivedEvent event, List<RollSpecification> rolls) {
        super(event);
        specifications.addAll(rolls);

        for (RollSpecification spec : specifications)
            results.add(RollUtil.roll(
                    getAuthor(), RngUtil.getRng(getAuthor()), spec.getnRolls(), spec.getDiceType()));

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s rolls ", getAuthorName()));

        for (RollSpecification spec : specifications)
            sb.append(spec.toString()).append(" ");
        sb.append("\n");

        for (DiceResult result : results) {
            for (int i = 0; i < result.getSpecification().getnRolls(); i++) {
                sb.append(
                        RollUtil.rollString(
                                String.format("%s rolls a ", getAuthorName()),
                                result.getEmoji().get(i),
                                result.getDiceType(),
                                result.getEffectiveResult().get(i),
                                result.getModifier(),
                                0
                        )
                ).append("\n");
            }
        }

        return sb.toString();
    }
}
