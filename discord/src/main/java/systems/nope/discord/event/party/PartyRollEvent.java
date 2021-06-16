package systems.nope.discord.event.party;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.*;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.model.Party;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PartyRollEvent extends DiceEvent {

    public static PartyRollEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 1)
            return new PartyRollEvent(event, 0);

        if (command.length == 2) {
            int mod;
            try {
                mod = Integer.parseInt(command[1]);
            } catch (NumberFormatException e) {
                throw new ParseException(String.format("'%s' is not a valid number, sir...'", command[1]));
            }

            return new PartyRollEvent(event, mod);
        }

        throw new ParseException("Sir, please use the command like !proll [MODIFIER]. The modifier is optional here.");
    }

    private Party party;
    private final Map<Member, DiceResult> results = new HashMap<>();
    private final Integer modifier;

    public PartyRollEvent(MessageReceivedEvent event) {
        this(event, 0);
    }

    public PartyRollEvent(MessageReceivedEvent event, Integer modifier) {
        super(event);
        this.modifier = modifier;

        // check if a party with member as the host exists
        Optional<Party> optionalHostedParty = PartyUtil.getHostedPartyOfMember(getAuthor());

        if (optionalHostedParty.isEmpty())
            return;

        party = optionalHostedParty.get();

        List<Member> partyMembers = party.getMembers();

        // check if there is no one in there
        if (partyMembers.size() == 0)
            return;

        //iterate over partyMembers
        for (Member member : partyMembers)
            results.put(
                    member,
                    RollUtil.roll(
                            member,
                            RngUtil.getRng(member),
                            1,
                            DiceUtils.getDiceTypeForMember(member),
                            modifier
                    )
            );
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
            DiceResult result = results.get(member);
            messageBuilder
                    .append(
                            RollUtil.rollString(
                                    String.format("%s's roll: ", getAuthorName()),
                                    result.getEmoji().get(0),
                                    result.getDiceType(),
                                    result.getResult().get(0),
                                    result.getModifier(),
                                    modifier
                            ))
                    .append("\n");
        }

        return messageBuilder.toString();
    }
}
