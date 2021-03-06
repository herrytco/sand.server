package systems.nope.discord.model;

import net.dv8tion.jda.api.entities.Member;
import systems.nope.discord.util.DiscordUtil;

import java.util.LinkedList;
import java.util.List;

public class Party {
    private final List<Member> members;
    private final Member leader;

    public Party(List<Member> members, Member leader) {
        this.members = new LinkedList<>();
        this.leader = leader;

        for (Member member : members) {
            if (member.getUser().isBot())
                continue;

            if (member.equals(leader))
                continue;

            this.members.add(member);
        }
    }

    public String getMemberString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, membersSize = members.size(); i < membersSize; i++) {
            Member member = members.get(i);

            sb.append(DiscordUtil.getMemberName(member));

            if (i < members.size() - 1)
                sb.append(", ");
        }

        return sb.toString();
    }

    public List<Member> getMembers() {
        return members;
    }

    public Member getLeader() {
        return leader;
    }
}
