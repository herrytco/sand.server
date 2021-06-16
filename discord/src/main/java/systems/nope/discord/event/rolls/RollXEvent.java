package systems.nope.discord.event.rolls;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.DiceUtils;
import systems.nope.discord.util.RngUtil;
import systems.nope.discord.util.RollUtil;
import systems.nope.discord.util.StringUtil;

public class RollXEvent extends DiceEvent {

    public static RollXEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 1)
            throw new ParseException("Sir, please use the command like !rollx <X> [MODIFIER]. The modifier is optional here, as usual.");

        if (command.length >= 2) {
            int nRolls;
            try {
                nRolls = Integer.parseInt(command[1]);
            } catch (NumberFormatException e) {
                throw new ParseException(String.format("'%s' is not a valid number, sir..., have you ever tried rolling '%s' dice?", command[1], command[1]));
            }

            if (command.length == 2)
                return new RollXEvent(event, nRolls);

            if (command.length == 3) {
                int modifier;
                try {
                    modifier = Integer.parseInt(command[2]);
                } catch (NumberFormatException e) {
                    throw new ParseException(String.format("'%s' is not a valid number, sir..., have you ever tried adding '%s' to something? Idiot...", command[1], command[1]));
                }

                return new RollXEvent(event, nRolls, modifier);
            }
        }

        throw new ParseException("Sir, please use the command like !rollx [MODIFIER]. The modifier is optional here.");
    }

    private final DiceResult results;
    private final int numberOfRolls;
    private final int modifier;

    public RollXEvent(MessageReceivedEvent event, int numberOfRolls) {
        this(event, numberOfRolls, 0);
    }

    public RollXEvent(MessageReceivedEvent event, int numberOfRolls, int modifier) {
        super(event);
        this.numberOfRolls = numberOfRolls;
        this.modifier = modifier;

        results = RollUtil.roll(
                getAuthor(),
                RngUtil.getRng(getAuthor()),
                this.numberOfRolls,
                DiceUtils.getDiceTypeForMember(getAuthor()),
                DiceUtils.getDiceModifierForMember(getAuthor())
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s rolls %d dice:\n", getAuthorName(), numberOfRolls));

        for (int i = 0; i < results.getSpecification().getnRolls(); i++)
            sb.append(
                String.format("%s\n",
                    RollUtil.rollString(
                        String.format("%s roll:",
                        StringUtil.integerToOrderedString(i + 1)),
                        results.getEmoji().get(i),
                        results.getDiceType(),
                        results.getResult().get(i),
                        results.getModifier(),
                        this.modifier
                    )
                )
            );

        return sb.toString();
    }

    public DiceResult getResults() {
        return results;
    }

    public int getNumberOfRolls() {
        return numberOfRolls;
    }

    public int getModifier() {
        return modifier;
    }
}
