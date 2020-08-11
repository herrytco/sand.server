package systems.nope.discord.eventlistener.dice.person.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceResult;
import systems.nope.discord.eventlistener.dice.DiceUtils;
import systems.nope.discord.eventlistener.dice.ServerConstants;
import systems.nope.discord.eventlistener.dice.event.DiceEvent;
import systems.nope.discord.eventlistener.dice.person.LinkUtils;
import systems.nope.discord.eventlistener.dice.person.Person;
import systems.nope.discord.eventlistener.dice.person.Stat;

import java.util.Optional;

public class AttributeRoleEvent extends DiceEvent {

    private Stat statToRollOn;
    private String message;
    private DiceResult result;

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

        result = DiceUtils.rollOnce(getAuthor());

        message = String.format("%s rolled with %s on %s(%s)\n",
                getAuthorName(), person.getName(), statToRollOn.getNameShort(), statToRollOn.getName())
                + DiceUtils.getCalculation(result) + "\n"
                + String.format(
                "%s%d + %s%d = %d",
                DiceUtils.getEmojiForResult(getAuthor(), result.getResult()),
                result.getEffectiveResult(),
                ServerConstants.emoteAttributeIcon,
                statToRollOn.getValue(),
                result.getEffectiveResult() + statToRollOn.getValue()
        );
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Stat getStatToRollOn() {
        return statToRollOn;
    }

    public DiceResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        return message;
    }
}
