package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceResult;
import systems.nope.discord.eventlistener.dice.DiceUtils;

public class RollEvent extends DiceEvent {

    private final DiceResult result;

    public RollEvent(MessageReceivedEvent event) {
        super(event);

        this.result = DiceUtils.rollOnce(event.getMember());
    }

    @Override
    public String toString() {
        return DiceUtils.diceResultToString(getAuthorName(), result);
    }
}
