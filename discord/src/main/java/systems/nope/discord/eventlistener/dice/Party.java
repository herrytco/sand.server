package systems.nope.discord.eventlistener.dice;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Party {
    public List<Member> members = new ArrayList<Member>();
    public Member leader;
}
