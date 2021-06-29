package systems.nope.discord.event.person;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.exceptions.NotFoundException;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.util.LinkUtils;
import systems.nope.discord.model.person.Person;

import java.io.IOException;

public class LinkEvent extends DiceEvent {

    public static LinkEvent fromString(MessageReceivedEvent event, String... command) throws ParseException {
        if (command.length == 1)
            return new LinkEvent(event, null);
        else if (command.length == 2)
            return new LinkEvent(event, command[1]);

        throw new ParseException("Sir, please use the command like !link <API-KEY>. The key can usually be found in your character sheet.");
    }

    private final Person linkedPerson;
    private final String message;

    public LinkEvent(MessageReceivedEvent event, String apiKey) throws ParseException {
        super(event);

        if (apiKey == null)
            try {
                linkedPerson = LinkUtils.relinkPerson(event.getMember());
            } catch (IOException e) {
                throw new ParseException("We currently experience some technical difficulties. It seems the queen does not want you to play today.");
            } catch (NotFoundException e) {
                throw new ParseException("Sir, I have no api key for you today, may I kindly ask you for a new one?");
            }
        else
            try {
                linkedPerson = LinkUtils.linkMemberToPersonIdentifiedByApiKey(getAuthor(), apiKey);
            } catch (IOException e) {
                throw new ParseException("We currently experience some technical difficulties. It seems the queen does not want you to play today.");
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
