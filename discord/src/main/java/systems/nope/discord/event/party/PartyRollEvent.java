package systems.nope.discord.event.party;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.DiceUtils;
import systems.nope.discord.util.DiscordUtil;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.model.Party;
import systems.nope.discord.util.PartyUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PartyRollEvent extends DiceEvent {
    private Party party;
    private final Map<Member, DiceResult> results = new HashMap<>();

    public PartyRollEvent(MessageReceivedEvent event) {
        super(event);

        // check if a party with member as the host exists
        Optional<Party> optionalHostedParty = PartyUtil.getHostedPartyOfMember(getAuthor());

        if (optionalHostedParty.isEmpty())
            return;

        party = optionalHostedParty.get();

        List<Member> partyMembers = party.getMembers();

        // check if there is no one in there
        if (partyMembers.size() == 0)
            return;

        // do the rolling


        //iterate over partyMembers
        for (Member member : partyMembers)
            results.put(member, DiceUtils.rollOnce(member));
    }

    public Party getParty() {
        return party;
    }

    public Map<Member, DiceResult> getResults() {
        return results;
    }

    @Override
    public String toString() {
        if (party == null)
            return "Maybe organize a !party first.";

        if (party.getMembers().size() == 0)
            return "This party is boring! I won't perform for it.";

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Rolling once for each party member:\n");

        for (Member member : party.getMembers()) {
            messageBuilder.append(DiceUtils.diceResultToString(DiscordUtil.getMemberName(member), results.get(member)));
            messageBuilder.append('\n');
        }

        return messageBuilder.toString();
    }
}
