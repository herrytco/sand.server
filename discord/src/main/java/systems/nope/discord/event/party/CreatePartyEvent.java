package systems.nope.discord.event.party;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.util.DiscordUtil;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.model.Party;
import systems.nope.discord.util.PartyUtil;

public class CreatePartyEvent extends DiceEvent {
    private final String message;

    public CreatePartyEvent(MessageReceivedEvent event) {
        super(event);
        Party party;

        try {
            party = PartyUtil.createParty(getAuthor());
        } catch (IllegalStateException e) {
            message = e.getMessage();
            return;
        }

        message = String.format(
                "%s %s now partying with %s as the host! \uD83C\uDF89",
                party.getMemberString(),
                party.getMembers().size() == 1 ? "is" : "are",
                DiscordUtil.getMemberName(party.getLeader()));
    }

    @Override
    public String toString() {
        return message;
    }
}
