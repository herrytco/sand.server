package systems.nope.discord.eventlistener.dice.party.event;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.eventlistener.dice.DiceUtils;
import systems.nope.discord.eventlistener.dice.event.DiceEvent;
import systems.nope.discord.eventlistener.dice.party.Party;
import systems.nope.discord.eventlistener.dice.party.PartyUtil;

import java.util.List;
import java.util.Optional;

public class PartyRollXEvent extends DiceEvent {
    final int numberOfRolls;
    final String message;

    public PartyRollXEvent(MessageReceivedEvent event, int numberOfRolls) {
        super(event);
        this.numberOfRolls = numberOfRolls;

        // check if a party with member as the host exists
        Optional<Party> optionalHostedParty = PartyUtil.getHostedPartyOfMember(getAuthor());

        if (optionalHostedParty.isEmpty()) {
            message = "Maybe organize a !party first.";
            return;
        }

        Party hostedParty = optionalHostedParty.get();

        List<Member> partyMembers = hostedParty.getMembers();

        // check if there is no one in there
        if (partyMembers.size() == 0) {
            message = "This party is boring! I won't perform for it.";
            return;
        }

        // do the rolling
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("Everybody in the party\uD83C\uDF89 rolls %d dice\n", numberOfRolls));

        for (Member member : partyMembers) {
            for (int i = 0; i < numberOfRolls; i++)
                messageBuilder.append(DiceUtils.rollOnceString(member)).append("\n");
        }

        message = messageBuilder.toString();
    }

    @Override
    public String toString() {
        return message;
    }
}
