package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceUtils;

public class DiceModifierEvent extends DiceEvent {
    final int diceModifier;

    public DiceModifierEvent(MessageReceivedEvent event, int diceModifier) {
        super(event);
        this.diceModifier = diceModifier;

        DiceUtils.setModifierForMember(getAuthor(), diceModifier);
    }

    @Override
    public String toString() {
        return String.format("%s has now a %s of %s%d.", getAuthorName(), "dice modifier", diceModifier > 0 ? "+" : "", diceModifier);
    }
}
