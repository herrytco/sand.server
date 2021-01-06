package systems.nope.discord.event.rolls;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.DiceUtils;
import systems.nope.discord.util.RngUtil;
import systems.nope.discord.util.RollUtil;

public class RollEvent extends DiceEvent {

    public static RollEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 1)
            return new RollEvent(event);

        if(command.length == 2) {
            int mod;
            try {
                mod = Integer.parseInt(command[1]);
            } catch (NumberFormatException e) {
                throw new ParseException(String.format("'%s' is not a valid number, sir...'", command[1]));
            }

            return new RollEvent(event, mod);
        }

        throw new ParseException("Sir, please use the command like !roll [MODIFIER]. The modifier is optional here.");
    }

    private final DiceResult result;
    private final Integer modifier;

    public RollEvent(MessageReceivedEvent event, int modifier) {
        super(event);

        Member member = getAuthor();
        this.modifier = modifier;

        this.result = RollUtil.roll(
                getAuthor(),
                RngUtil.getRng(member),
                1,
                DiceUtils.getDiceTypeForMember(member),
                DiceUtils.getDiceModifierForMember(member)
        );
    }

    public RollEvent(MessageReceivedEvent event) {
        this(event, 0);
    }

    public DiceResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        return RollUtil.rollString(
                String.format("%s rolled a", getAuthorName()),
                result.getEmoji().get(0),
                result.getDiceType(),
                result.getResult().get(0),
                result.getModifier(),
                modifier
        );
    }
}
