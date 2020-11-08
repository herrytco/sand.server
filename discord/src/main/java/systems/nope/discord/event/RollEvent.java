package systems.nope.discord.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.DiceUtils;

public class RollEvent extends DiceEvent {

    private final DiceResult result;

    public RollEvent(MessageReceivedEvent event) {
        super(event);

        this.result = DiceUtils.rollOnce(event.getMember());
    }

    public DiceResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        return DiceUtils.diceResultToString(getAuthorName(), result);
    }
}
