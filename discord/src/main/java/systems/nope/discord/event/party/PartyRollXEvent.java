package systems.nope.discord.event.party;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.model.DiceResult;
import systems.nope.discord.util.*;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.model.Party;

import java.util.*;

public class PartyRollXEvent extends DiceEvent {

    public static PartyRollXEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 1)
            throw new ParseException("Sir, please use the command like !prollx <X> [MODIFIER]. The modifier is optional here, as usual.");

        if (command.length >= 2) {
            int nRolls;
            try {
                nRolls = Integer.parseInt(command[1]);
            } catch (NumberFormatException e) {
                throw new ParseException(String.format("'%s' is not a valid number, sir..., have you ever tried rolling '%s' dice?", command[1], command[1]));
            }

            if (command.length == 2)
                return new PartyRollXEvent(event, nRolls);

            if (command.length == 3) {
                int modifier;
                try {
                    modifier = Integer.parseInt(command[2]);
                } catch (NumberFormatException e) {
                    throw new ParseException(String.format("'%s' is not a valid number, sir..., have you ever tried adding '%s' to something? Idiot...", command[1], command[1]));
                }

                return new PartyRollXEvent(event, nRolls, modifier);
            }
        }

        throw new ParseException("Sir, please use the command like !prollx [MODIFIER]. The modifier is optional here.");
    }

    private final int numberOfRolls;
    private final int modifier;
    private final Map<Member, DiceResult> results = new HashMap<>();
    private Party party;

    public PartyRollXEvent(MessageReceivedEvent event, int numberOfRolls) {
        this(event, numberOfRolls, 0);
    }

    public PartyRollXEvent(MessageReceivedEvent event, int numberOfRolls, int modifier) {
        super(event);
        this.numberOfRolls = numberOfRolls;
        this.modifier = modifier;

        // check if a party with member as the host exists
        Optional<Party> optionalHostedParty = PartyUtil.getHostedPartyOfMember(getAuthor());

        if (optionalHostedParty.isEmpty())
            return;

        party = optionalHostedParty.get();

        for (Member member : party.getMembers())
            results.put(member, RollUtil.roll(
                    member,
                    RngUtil.getRng(member),
                    numberOfRolls,
                    DiceUtils.getDiceTypeForMember(member),
                    DiceUtils.getDiceModifierForMember(member)
            ));
    }

    public int getNumberOfRolls() {
        return numberOfRolls;
    }

    public Map<Member, DiceResult> getResults() {
        return results;
    }

    public Party getParty() {
        return party;
    }

    private String singleResultToString(Member member, DiceResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append(
                String.format(
                        "%s rolls %d dice:\n",
                        DiscordUtil.getMemberName(member),
                        result.getSpecification().getnRolls())
        );

        for (int i = 0; i < result.getSpecification().getnRolls(); i++)
            sb.append(
                    String.format("%s\n",
                            RollUtil.rollString(
                                    String.format("%s roll:",
                                            StringUtil.integerToOrderedString(i + 1)),
                                    result.getEmoji().get(i),
                                    result.getDiceType(),
                                    result.getResult().get(i),
                                    result.getModifier(),
                                    this.modifier
                            )
                    )
            );

        return sb.toString();
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

        for (Member member : party.getMembers()) {
            DiceResult result = results.get(member);
            messageBuilder.append(
                    singleResultToString(member, result)
            ).append("\n");
        }

        return messageBuilder.toString();
    }
}
