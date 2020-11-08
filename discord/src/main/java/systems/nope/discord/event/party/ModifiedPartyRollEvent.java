package systems.nope.discord.event.party;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.util.DiceUtils;
import systems.nope.discord.util.DiscordUtil;

public class ModifiedPartyRollEvent extends PartyRollEvent {
    private final int modifier;

    public ModifiedPartyRollEvent(MessageReceivedEvent event, int modifier) {
        super(event);
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        if (getParty() == null)
            return "Maybe organize a !party first.";

        if (getParty().getMembers().size() == 0)
            return "This party is boring! I won't perform for it.";

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Rolling once for each party member:\n");

        for (Member member : getParty().getMembers()) {
            messageBuilder
                    .append(DiceUtils.diceResultToString(DiscordUtil.getMemberName(member), getResults().get(member)))
                    .append(DiceUtils.getDiceModificationString(getResults().get(member), modifier))
                    .append('\n');
        }

        return messageBuilder.toString();
    }
}
