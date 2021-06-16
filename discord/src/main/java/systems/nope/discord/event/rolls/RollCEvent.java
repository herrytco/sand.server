package systems.nope.discord.event.rolls;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.util.RollUtil;
import systems.nope.discord.util.StringUtil;

public class RollCEvent extends RollXEvent {

    public static RollCEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 1)
            throw new ParseException("Sir, please use the command like !rollc <X> [MODIFIER]. The modifier is optional here, as you know.");

        if (command.length >= 2) {
            int nRolls;
            try {
                nRolls = Integer.parseInt(command[1]);
            } catch (NumberFormatException e) {
                throw new ParseException(String.format("'%s' is not a valid number, sir..., have you ever tried rolling '%s' dice?", command[1], command[1]));
            }

            if (command.length == 2)
                return new RollCEvent(event, nRolls);

            if (command.length == 3) {
                int modifier;
                try {
                    modifier = Integer.parseInt(command[2]);
                } catch (NumberFormatException e) {
                    throw new ParseException(String.format("'%s' is not a valid number, sir..., have you ever tried adding '%s' to something? Idiot...", command[1], command[1]));
                }

                return new RollCEvent(event, nRolls, modifier);
            }
        }

        throw new ParseException("SIR, please use the command like !roll [MODIFIER]. The modifier is optional here.");
    }

    public RollCEvent(MessageReceivedEvent event, Integer numberOfRolls) {
        this(event, numberOfRolls, 0);
    }

    public RollCEvent(MessageReceivedEvent event, Integer numberOfRolls, Integer modifier) {
        super(event, numberOfRolls, modifier);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s rolls %d cumulative dice:\n", getAuthorName(), getNumberOfRolls()));
        int sum = 0;

        for (int i = 0; i < getResults().getSpecification().getnRolls(); i++) {
            sum += getResults().getResult().get(i) + getResults().getModifier() + getModifier();

            sb.append(
                    String.format("%s\n",
                            RollUtil.rollString(
                                    String.format("%s' %s roll:",
                                            getAuthorName(),
                                            StringUtil.integerToOrderedString(i + 1)),
                                    getResults().getEmoji().get(i),
                                    getResults().getDiceType(),
                                    getResults().getResult().get(i),
                                    getResults().getModifier(),
                                    getModifier()
                            )
                    )
            );
        }
        sb.append(String.format("The total sum is ... *furiously typing on calculator* ... %s!", sum));

        return sb.toString();
    }


}
