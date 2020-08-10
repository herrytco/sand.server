package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceUtils;

public class AverageResetEvent extends DiceEvent {

    private final AverageScope scope;

    public AverageResetEvent(MessageReceivedEvent event, AverageScope scope) {
        super(event);
        this.scope = scope;
    }

    public AverageScope getScope() {
        return scope;
    }

    @Override
    public void handle() {
        if (!handled) {
            switch (scope) {
                case User:
                    DiceUtils.resetAvgOfMember(getAuthor());
                    break;
                case Server:
                    DiceUtils.resetAvgOfSever();
                    break;
            }

            super.handle();
        }
    }

    @Override
    public String toString() {
        switch (scope) {
            case Server:
                return "I forgot everything each of you did ... Who are you guys again?";
            case User:
                return String.format("I forgot everything what %s rolled.", getAuthorName());
        }

        return "";
    }
}
