package systems.nope.discord.eventlistener.dice.party.event;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceResult;
import systems.nope.discord.eventlistener.dice.DiceUtils;
import systems.nope.discord.eventlistener.dice.event.DiceEvent;
import systems.nope.discord.eventlistener.dice.party.Party;
import systems.nope.discord.eventlistener.dice.party.PartyUtil;

import java.util.*;

public class PartyRollXEvent extends DiceEvent {
    private final int numberOfRolls;
    private final Map<Member, List<DiceResult>> results = new HashMap<>();
    private Party party;

    public PartyRollXEvent(MessageReceivedEvent event, int numberOfRolls) {
        super(event);
        this.numberOfRolls = numberOfRolls;

        // check if a party with member as the host exists
        Optional<Party> optionalHostedParty = PartyUtil.getHostedPartyOfMember(getAuthor());

        if (optionalHostedParty.isEmpty())
            return;

        party = optionalHostedParty.get();

        for (Member member : party.getMembers()) {
            results.put(member, new LinkedList<>());

            for (int i = 0; i < numberOfRolls; i++)
                results.get(member).add(DiceUtils.rollOnce(member));
        }
    }

    public int getNumberOfRolls() {
        return numberOfRolls;
    }

    public Map<Member, List<DiceResult>> getResults() {
        return results;
    }

    public Party getParty() {
        return party;
    }

    @Override
    public String toString() {
        if (party == null)
            return "Maybe organize a !party first.";

        if (party.getMembers().size() == 0)
            return "This party is boring! I won't perform for it.";

        // do the rolling
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("Everybody in the party\uD83C\uDF89 rolls %d dice\n", numberOfRolls));

        for (Member member : party.getMembers())
            for (DiceResult result : results.get(member))
                messageBuilder.append(DiceUtils.diceResultToString(member.getEffectiveName(), result))
                        .append("\n");

        return messageBuilder.toString();
    }
}
