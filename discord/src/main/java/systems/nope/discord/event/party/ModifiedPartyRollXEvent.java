package systems.nope.discord.event.party;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.DiceUtils;
import systems.nope.discord.util.DiscordUtil;

public class ModifiedPartyRollXEvent extends PartyRollXEvent {
    private final int modifier;

    public ModifiedPartyRollXEvent(MessageReceivedEvent event, int numberOfRolls, int modifier) {
        super(event, numberOfRolls);
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        if (getParty() == null)
            return "Maybe organize a !party first.";

        if (getParty().getMembers().size() == 0)
            return "This party is boring! I won't perform for it.";

        // do the rolling
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(
                String.format("Everybody in the party\uD83C\uDF89 rolls %d dice with a modifier of %d\n",
                        getNumberOfRolls(), modifier)
        );

        for (Member member : getParty().getMembers())
            for (DiceResult result : getResults().get(member))
                messageBuilder.append(DiceUtils.diceResultToString(DiscordUtil.getMemberName(member), result))
                        .append(DiceUtils.getDiceModificationString(result, modifier))
                        .append("\n");

        return messageBuilder.toString();
    }
}
