package systems.nope.discord.event.person;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.util.LinkUtils;
import systems.nope.discord.model.person.Person;

import java.io.IOException;

public class LinkEvent extends DiceEvent {

    private Person linkedPerson;
    private String message;

    public LinkEvent(MessageReceivedEvent event, String apiKey) {
        super(event);

        try {
            linkedPerson = LinkUtils.linkMemberToPersonIdentifiedByApiKey(getAuthor(), apiKey);
        } catch (IOException e) {
            this.message = "Problem during fetching of player data.";
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if (linkedPerson == null)
            this.message = "No playerdata could be found for this API Key!";
        else {
            this.message = String.format("Linked '%s' to Character '%s'.", getAuthorName(), linkedPerson.getName());
        }
    }

    public Person getLinkedPerson() {
        return linkedPerson;
    }

    @Override
    public String toString() {
        return message;
    }
}
