package systems.nope.discord.util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import systems.nope.discord.exceptions.NotFoundException;
import systems.nope.discord.exceptions.ServerException;
import systems.nope.discord.model.person.Person;
import systems.nope.discord.file.LinkFileManager;
import systems.nope.discord.service.PersonService;

import java.io.IOException;
import java.util.*;

public class LinkUtils {
    private static final LinkFileManager fileManager = new LinkFileManager();

    /**
     * @param member - discord user
     * @return the stored person for a member
     */
    public static Optional<Person> getPersonForMember(Member member) {
        try {
            Person person = relinkPerson(member);

            if (person != null)
                return Optional.of(person);
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Tries to reestablish the link with the persisted apiKey.
     *
     * @param member - discord user
     * @return retrieved World.Seed character from the backend
     * @throws IOException - problem in the filesystem
     */
    public static Person relinkPerson(Member member) throws IOException, NotFoundException {
        String key = (String) fileManager.getValue(member.getId(), "links.json");

        if (key == null)
            throw new NotFoundException();

        return linkMemberToPersonIdentifiedByApiKey(member, key);
    }

    /**
     * @param member - discord user
     * @param apiKey - 256 character ID of a World.Seed character
     * @return the retrieved person for a member
     */
    public static Person linkMemberToPersonIdentifiedByApiKey(Member member, String apiKey) throws IOException {
        try {
            Person person = PersonService.getPersonForApiKey(apiKey);

            renameMemberToPerson(member, person);
            storeLink(member, apiKey);

            return person;
        } catch (ServerException e) {
            return null;
        }
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
     * renames the discord user to the name of the character and persists the original name in the filesystem
     *
     * @param member - discord user
     * @param person - World.Seed character
     * @throws IOException - problem in the filesystem
     */
    public static void renameMemberToPerson(Member member, Person person) throws IOException {
        try {
            if (fileManager.getValue(member.getId(), "names.json") == null)
                fileManager.putKeyValuePair(member.getId(), DiscordUtil.getMemberName(member), "names.json");
            member.modifyNickname(person.getName()).queue();
        } catch (HierarchyException e) {
            System.out.printf("Cannot rename %s due to hierarchy issues.%n", member.getEffectiveName());
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
                System.out.printf("Cannot rename %s due to hierarchy issues.\nMessage: %s%n", member.getEffectiveName(), e.getMessage());
            }
        }
    }


}