package systems.nope.discord.event.person;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.constants.EmoteConstants;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.model.person.Person;
import systems.nope.discord.model.person.StatSheet;
import systems.nope.discord.model.person.StatValue;
import systems.nope.discord.util.DiceUtils;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.LinkUtils;
import systems.nope.discord.util.RngUtil;
import systems.nope.discord.util.RollUtil;

import java.util.Optional;

public class AttributeRoleEvent extends DiceEvent {

    public static AttributeRoleEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length < 2 || command.length > 3)
            throw new ParseException("Sir, please use the command like !rolla <ATTRIBUTE> [MODIFIER]. The attribute ID can usually be found in your character sheet.");

        if (command.length == 2)
            return new AttributeRoleEvent(event, command[1]);

        int mod;

        try {
            mod = Integer.parseInt(command[2]);
        } catch (NumberFormatException e) {
            throw new ParseException(String.format("'%s' is not a valid number, sir...'", command[2]));
        }

        return new AttributeRoleEvent(event, command[1], mod);
    }

    private StatValue statToRollOn;
    private String message;
    private DiceResult result;
    private final int modifier;

    public AttributeRoleEvent(MessageReceivedEvent event, String attribute) {
        this(event, attribute, 0);
    }

    public AttributeRoleEvent(MessageReceivedEvent event, String attribute, int modifier) {
        super(event);
        this.modifier = modifier;

        Optional<Person> linkedPerson = LinkUtils.getPersonForMember(getAuthor());

        if (linkedPerson.isEmpty()) {
            message = "No link established, please !link yourself with a character first ... moron.";
            return;
        }

        Person person = linkedPerson.get();

        for (StatSheet sheet : person.getStatSheets()) {
            for (StatValue value : sheet.getValues()) {

                if (value.getName().toLowerCase().equals(attribute.toLowerCase())
                        || value.getNameShort().toLowerCase().equals(attribute.toLowerCase())) {
                    statToRollOn = value;
                    break;
                }
            }

            if (statToRollOn != null)
                break;
        }

        if (statToRollOn == null) {
            message = String.format("The stat '%s' does not exist, check your stats again please.", attribute);
            return;
        }

        result = RollUtil.roll(
                getAuthor(),
                RngUtil.getRng(getAuthor()),
                1,
                DiceUtils.getDiceTypeForMember(getAuthor()),
                DiceUtils.getDiceModifierForMember(getAuthor())
        );

        StringBuilder sb = new StringBuilder(
                String.format(
                        "%s rolled on %s(%s)\n",
                        getAuthorName(),
                        statToRollOn.getNameShort(),
                        statToRollOn.getName())
        )
                .append(
                        RollUtil.rollString(
                                result.getEmoji().get(0),
                                result.getDiceType(),
                                result.getResult().get(0),
                                result.getModifier(),
                                modifier
                        )
                ).append("\n")
                .append(
                        String.format("%s%d + %s%d = %d",
                                result.getEmoji().get(0),
                                result.getEffectiveResult(modifier).get(0),
                                EmoteConstants.emoteAttributeIcon,
                                statToRollOn.getValue(),
                                statToRollOn.getValue() + result.getEffectiveResult(modifier).get(0)
                        )
                );

        message = sb.toString();
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
