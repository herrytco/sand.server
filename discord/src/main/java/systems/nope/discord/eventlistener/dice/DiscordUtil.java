package systems.nope.discord.eventlistener.dice;

import net.dv8tion.jda.api.entities.Member;

public class DiscordUtil {
    public static String getMemberName(Member member) {
        if(member.getNickname() != null)
            return member.getNickname();

        return member.getUser().getName();
    }
}
