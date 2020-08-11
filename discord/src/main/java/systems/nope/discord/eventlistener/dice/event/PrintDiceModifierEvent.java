package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceUtils;

public class PrintDiceModifierEvent extends DiceEvent {
    private final int diceModifier;

    public PrintDiceModifierEvent(MessageReceivedEvent event) {
        super(event);

        diceModifier = DiceUtils.getDiceModifierForMember(getAuthor());
    }

    @Override
    public String toString() {
        return String.format("Sir, your current dice modifier is %s%d", diceModifier > 0 ? "+" : "", diceModifier);
    }
}
