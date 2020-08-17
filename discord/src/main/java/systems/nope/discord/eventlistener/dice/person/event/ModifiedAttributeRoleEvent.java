package systems.nope.discord.eventlistener.dice.person.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceUtils;

public class ModifiedAttributeRoleEvent extends AttributeRoleEvent {

    public ModifiedAttributeRoleEvent(MessageReceivedEvent event, String attribute, int modifier) {
        super(event, attribute);

        setMessage(getMessage()
                + DiceUtils.getDiceModificationString(getResult(), modifier,
                getResult().getEffectiveResult() + modifier + getStatToRollOn().getValue()));
    }
}
