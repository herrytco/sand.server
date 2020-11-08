package systems.nope.discord.event.person.roll;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.constants.EmoteConstants;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.model.person.Person;
import systems.nope.discord.model.person.StatSheet;
import systems.nope.discord.model.person.StatValue;
import systems.nope.discord.util.DiceUtils;
import systems.nope.discord.constants.ServerConstants;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.LinkUtils;

import java.util.Optional;

public class AttributeRoleEvent extends DiceEvent {

    private StatValue statToRollOn;
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

        for(StatSheet sheet : person.getStatSheets()) {
            for(StatValue value : sheet.getValues()) {

                if(value.getName().toLowerCase().equals(attribute.toLowerCase())
                || value.getNameShort().toLowerCase().equals(attribute.toLowerCase())) {
                    statToRollOn = value;
                    break;
                }
            }

            if(statToRollOn != null)
                break;
        }

        if (statToRollOn == null) {
            message = String.format("The stat '%s' does not exist, check your stats again please.", attribute);
            return;
        }

        result = DiceUtils.rollOnce(getAuthor());

        message = String.format("%s rolled on %s(%s)\n",
                person.getName(), statToRollOn.getNameShort(), statToRollOn.getName())
                + DiceUtils.getCalculation(result) + "\n"
                + String.format(
                "%s%d + %s%d = %d",
                DiceUtils.getEmojiForResult(getAuthor(), result.getResult()),
                result.getEffectiveResult(),
                EmoteConstants.emoteAttributeIcon,
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

    public StatValue getStatToRollOn() {
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