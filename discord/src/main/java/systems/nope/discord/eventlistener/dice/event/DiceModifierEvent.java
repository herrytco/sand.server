package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceUtils;

public class DiceModifierEvent extends DiceEvent {
    final int diceType;

    public DiceModifierEvent(MessageReceivedEvent event, int diceType) {
        super(event);
        this.diceType = diceType;

        DiceUtils.setModifierForMember(getAuthor(), diceType);
    }

    @Override
    public String toString() {
        return String.format("%s has now a %s of %d.", getAuthorName(), "dice modifier", diceType);
    }
}
