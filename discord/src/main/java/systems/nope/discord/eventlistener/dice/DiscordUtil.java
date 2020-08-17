package systems.nope.discord.eventlistener.dice;

import net.dv8tion.jda.api.entities.Member;

public class DiscordUtil {
    /**
     * @param member - discord user
     * @return the name of a member with precedence to the nickname
     */
    public static String getMemberName(Member member) {
        if (member.getNickname() != null)
            return member.getNickname();

        return member.getUser().getName();
    }
}
