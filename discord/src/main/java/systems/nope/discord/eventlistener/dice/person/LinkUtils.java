package systems.nope.discord.eventlistener.dice.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import okhttp3.Request;
import okhttp3.Response;
import systems.nope.discord.eventlistener.dice.BackendUtil;
import systems.nope.discord.eventlistener.dice.ServerConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LinkUtils {
    private static final Map<Member, Person> playerChars = new HashMap<>();
    private static final Map<Member, String> nicknameStash = new HashMap<>();

    public static Optional<Person> getPersonForMember(Member member) {
        if(playerChars.containsKey(member))
            return Optional.of(playerChars.get(member));

        return Optional.empty();
    }

    public static boolean isMemberLinked(Member member) {
        return playerChars.containsKey(member);
    }

    public static boolean unlinkMember(Member member) {
        if (playerChars.containsKey(member)) {
            playerChars.remove(member);
            return true;
        }

        return false;
    }

    public static void revertPersonNicknamingFromMember(Member member) {
        if(nicknameStash.containsKey(member)) {
            try {
                String name = nicknameStash.remove(member);
                member.modifyNickname(name).queue();
            } catch (HierarchyException e) {
                System.out.println(String.format("Cannot rename %s due to hierarchy issues.\nMessage: %s", member.getEffectiveName(), e.getMessage()));
            }
        }
    }

    public static void renameMemberToPreson(Member member, Person person) {
        try {
            nicknameStash.put(member, member.getNickname());
            member.modifyNickname(person.getName()).queue();

        } catch (HierarchyException e) {
            System.out.println(String.format("Cannot rename %s due to hierarchy issues.", member.getEffectiveName()));
        }
    }

    public static Person linkMemberToPersonIdentifiedByApiKey(Member member, String apiKey) throws IOException {
        String token = BackendUtil.getToken();

        if (token != null) {
            Request request = new Request.Builder()
                    .url(String.format("%s/persons/api/%s", ServerConstants.urlBackend, apiKey))
                    .header("Authorization", String.format("Bearer %s", token))
                    .build();

            Response response = BackendUtil.httpClient.newCall(request).execute();
            HashMap<String, Object> data;

            ObjectMapper mapper = new ObjectMapper();
            try {
                data = mapper.readValue(response.body().string(), HashMap.class);

                Person linkedPerson = new Person((String) data.get("name"));
                linkedPerson.setStats((List<Map<String, Object>>) data.get("statValues"));

                if (linkedPerson.getName() != null) {
                    playerChars.put(member, linkedPerson);
                    renameMemberToPreson(member, linkedPerson);

                    return linkedPerson;
                }
            } catch (MismatchedInputException e) {
                System.out.println("No data for apiKey: " + apiKey);
                return null;
            }
        }

        return null;
    }
}