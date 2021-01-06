package systems.nope.discord.util;

import net.dv8tion.jda.api.entities.Member;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class RngUtil {
    private static final Map<Member, SecureRandom> rngs = new HashMap<>();

    public static SecureRandom getRng(Member member) {
        return rngs.getOrDefault(member, new SecureRandom());
    }
}
