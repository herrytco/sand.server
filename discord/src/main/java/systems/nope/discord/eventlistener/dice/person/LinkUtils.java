package systems.nope.discord.eventlistener.dice.person;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import okhttp3.Request;
import okhttp3.Response;
import systems.nope.discord.eventlistener.dice.backend.BackendUtil;
import systems.nope.discord.eventlistener.dice.DiscordUtil;
import systems.nope.discord.eventlistener.dice.ServerConstants;
import systems.nope.discord.eventlistener.dice.file.LinkFileManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LinkUtils {
    private static final Map<Member, Person> playerChars = new HashMap<>();
    private static final LinkFileManager fileManager = new LinkFileManager();

    /**
     * @param member - discord user
     * @return the stored person for a member
     */
    public static Optional<Person> getPersonForMember(Member member) {
        if (playerChars.containsKey(member))
            return Optional.of(playerChars.get(member));

        return Optional.empty();
    }

    /**
     * persists the link to the Person in the filesystem
     *
     * @param member - discord user
     * @param apiKey - 256 character ID of a World.Seed character
     */
    public static void storeLink(Member member, String apiKey) throws IOException {
        fileManager.putKeyValuePair(member.getId(), apiKey, "links.json");
    }

    /**
     * deletes the link to a Person
     *
     * @param member - discord user
     * @return if a link existed
     */
    public static boolean unlinkMember(Member member) throws IOException {
        if (playerChars.containsKey(member)) {
            playerChars.remove(member);
            return true;
        }

        fileManager.deleteKey(member.getId(), "links.json");

        return false;
    }

    /**
     * renames the discord user to the name of the character and persists the original name in the filesystem
     *
     * @param member - discord user
     * @param person - World.Seed character
     * @throws IOException - problem in the filesystem
     */
    public static void renameMemberToPerson(Member member, Person person) throws IOException {
        try {
            member.modifyNickname(person.getName()).queue();

            fileManager.putKeyValuePair(member.getId(), DiscordUtil.getMemberName(member), "names.json");

        } catch (HierarchyException e) {
            System.out.println(String.format("Cannot rename %s due to hierarchy issues.", member.getEffectiveName()));
        }
    }

    /**
     * restores the original nickname of the member from the filesystem (if one was persisted)
     *
     * @param member - discord user
     * @throws IOException - problem in the filesystem
     */
    public static void revertPersonNicknamingFromMember(Member member) throws IOException {
        String storedName = (String) fileManager.deleteKey(member.getId(), "names.json");

        if (storedName != null) {
            try {
                member.modifyNickname(storedName).queue();
            } catch (HierarchyException e) {
                System.out.println(String.format("Cannot rename %s due to hierarchy issues.\nMessage: %s", member.getEffectiveName(), e.getMessage()));
            }
        }
    }

    /**
     * Tries to reestablish the link with the persisted apiKey.
     *
     * @param member - discord user
     * @return retrieved World.Seed character from the backend
     * @throws IOException - problem in the filesystem
     */
    public static Person relinkPerson(Member member) throws IOException {
        String key = (String) fileManager.getValue(member.getId(), "links.json");

        return linkMemberToPersonIdentifiedByApiKey(member, key);
    }

    private static void enhancePerson(Person person) throws IOException {
        String sheets = BackendUtil.sendRequest(String.format("%s/stat-sheets/person/id/%d", ServerConstants.urlBackend, person.getId()));

        System.out.println(sheets);
    }

    /**
     * @param member - discord user
     * @param apiKey - 256 character ID of a World.Seed character
     * @return the retrieved person for a member
     */
    public static Person linkMemberToPersonIdentifiedByApiKey(Member member, String apiKey) throws IOException {
        String token = BackendUtil.getToken();

        if (token != null) {
            String response = BackendUtil.sendRequest(String.format("%s/persons/api/%s", ServerConstants.urlBackend, apiKey));

            System.out.println(response);

            ObjectMapper mapper = new ObjectMapper();
            try {
                HashMap<String, Object> data = mapper.readValue(response, HashMap.class);

                Person linkedPerson = Person.fromJson(data);

                playerChars.put(member, linkedPerson);

                enhancePerson(linkedPerson);

                renameMemberToPerson(member, linkedPerson);
                storeLink(member, apiKey);

                return linkedPerson;
            } catch (MismatchedInputException | JsonParseException e) {
                if (apiKey != null)
                    System.out.println("No data for apiKey: " + apiKey.substring(0, Math.min(apiKey.length(), 20)) + "...");
                else
                    System.out.println("No API key stored.");
                unlinkMember(member);
                return null;
            }
        }

        return null;
    }
}