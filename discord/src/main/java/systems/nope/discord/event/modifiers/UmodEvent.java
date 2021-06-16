package systems.nope.discord.event.modifiers;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.util.DiceUtils;

public class UmodEvent extends DiceEvent {
    final Integer diceModifier;

    public UmodEvent(MessageReceivedEvent event, Integer diceModifier) {
        super(event);
        this.diceModifier = diceModifier;

        if(diceModifier != null)
            DiceUtils.setModifierForMember(getAuthor(), diceModifier);
    }

    public static UmodEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if(command.length == 1)
            return new UmodEvent(event, null);

        if(command.length > 2)
            throw new ParseException("Please use the command like !umod <MODIFIER>, sir.");

        int mod;
        try {
            mod = Integer.parseInt(command[1]);
        } catch (NumberFormatException e) {
            throw new ParseException(String.format("'%s' is not a valid number, sir...'", command[1]));
        }

        return new UmodEvent(event, mod);
    }

    @Override
    public String toString() {
        if(diceModifier == null) {
            int mod = DiceUtils.getDiceModifierForMember(getAuthor());

            return String.format("%s has currently a modifier of %s%d, sir.",
                    getAuthorName(),
                    mod < 0 ? "-" : "+",
                    mod
            );
        }

        return String.format(
                "%s has now a %s of %s%d.",
                getAuthorName(),
                "dice modifier",
                diceModifier > 0 ? "+" : "",
                diceModifier
        );
    }
}
