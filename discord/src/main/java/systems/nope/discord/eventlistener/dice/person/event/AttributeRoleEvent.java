package systems.nope.discord.eventlistener.dice.person.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceResult;
import systems.nope.discord.eventlistener.dice.DiceUtils;
import systems.nope.discord.eventlistener.dice.event.DiceEvent;
import systems.nope.discord.eventlistener.dice.person.LinkUtils;
import systems.nope.discord.eventlistener.dice.person.Person;
import systems.nope.discord.eventlistener.dice.person.Stat;

import java.util.Optional;

public class AttributeRoleEvent extends DiceEvent {

    private Stat statToRollOn;
    private final String message;

    public AttributeRoleEvent(MessageReceivedEvent event, String attribute) {
        super(event);

        Optional<Person> linkedPerson = LinkUtils.getPersonForMember(getAuthor());

        if (linkedPerson.isEmpty()) {
            message = "No link established, please !link yourself with a character first ... moron.";
            return;
        }

        Person person = linkedPerson.get();

        for (Stat stat : person.getStats())
            if (stat.getName().toLowerCase().equals(attribute.toLowerCase())
                    || stat.getNameShort().toLowerCase().equals(attribute.toLowerCase())) {
                statToRollOn = stat;
                break;
            }

        if (statToRollOn == null) {
            message = String.format("The stat '%s' does not exist, check your stats again please.", attribute);
            return;
        }

        DiceResult diceResult = DiceUtils.rollOnce(getAuthor());

        message = String.format("%s rolled with %s on %s(%s)\n",
                getAuthorName(), person.getName(), statToRollOn.getNameShort(), statToRollOn.getName())
                + DiceUtils.getCalculation(diceResult) + "\n"
                + String.format(
                "%s%d + %d = %d",
                DiceUtils.getEmojiForResult(getAuthor(), diceResult.getResult()),
                diceResult.getEffectiveResult(), statToRollOn.getValue(),
                diceResult.getEffectiveResult() + statToRollOn.getValue()
        );
    }

    @Override
    public String toString() {
        return message;
    }
}
