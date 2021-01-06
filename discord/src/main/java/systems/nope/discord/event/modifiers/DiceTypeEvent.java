package systems.nope.discord.event.modifiers;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.util.DiceUtils;

public class DiceTypeEvent extends DiceEvent {

    public static DiceTypeEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 1)
            return new DiceTypeEvent(event, null);

        if (command.length > 2)
            throw new ParseException("Please use the command like !udice <MODIFIER>, sir.");

        int mod;
        try {
            mod = Integer.parseInt(command[1]);
        } catch (NumberFormatException e) {
            throw new ParseException(String.format("'%s' is not a valid number, sir...'", command[1]));
        }

        return new DiceTypeEvent(event, mod);
    }


    final Integer diceType;

    public DiceTypeEvent(MessageReceivedEvent event, Integer diceType) {
        super(event);
        this.diceType = diceType;

        if (diceType != null)
            DiceUtils.setDiceTypeForMember(getAuthor(), diceType);
    }

    @Override
    public String toString() {
        if (diceType == null)
            return String.format("%s currently uses a %d-sided dice.",
                    getAuthorName(),
                    DiceUtils.getDiceTypeForMember(getAuthor())
            );

        return String.format("%s now uses a %d-sided dice.", getAuthorName(), diceType);
    }
}
