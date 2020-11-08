package systems.nope.discord.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.util.DiceUtils;

public class DiceTypeEvent extends DiceEvent {
    final int diceType;

    public DiceTypeEvent(MessageReceivedEvent event, int diceType) {
        super(event);
        this.diceType = diceType;

        DiceUtils.setDiceTypeForMember(getAuthor(), diceType);
    }

    @Override
    public String toString() {
        return String.format("%s now uses a %d-sided dice.", getAuthorName(), diceType);
    }
}
