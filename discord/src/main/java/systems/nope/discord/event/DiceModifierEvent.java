package systems.nope.discord.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.util.DiceUtils;

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
