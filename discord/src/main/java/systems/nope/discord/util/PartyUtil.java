package systems.nope.discord.util;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import systems.nope.discord.model.Party;

import java.util.*;

public class PartyUtil {
    private static final Map<Member, Party> parties = new HashMap<>();

    public static Optional<Party> getHostedPartyOfMember(Member member) {
        Party party = parties.get(member);

        if (party != null)
            return Optional.of(party);

        return Optional.empty();
    }

    public static Party createParty(Member member) {
        GuildVoiceState voiceChannelOfMember = member.getVoiceState();

        if (voiceChannelOfMember == null || voiceChannelOfMember.getChannel() == null)
            throw new IllegalStateException("Uhm, if you really want to party, you should join a voicechannel first!");

        List<Member> members = new ArrayList<>(voiceChannelOfMember.getChannel().getMembers());

        if (members.size() <= 1)
            throw new IllegalStateException("Partying alone is lame ... Get some friends!");

        Party party = new Party(members, member);
        parties.put(party.getLeader(), party);

        return party;
    }
}
