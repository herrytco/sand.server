package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceUtils;

public class ModifiedRollEvent extends RollEvent {

    private final int modifier;

    public ModifiedRollEvent(MessageReceivedEvent event, int modifier) {
        super(event);

        this.modifier = modifier;
    }

    @Override
    public String toString() {
        String outputRegular = super.toString();

        return outputRegular + DiceUtils.getDiceModificationString(getResult(), modifier);
    }
}
