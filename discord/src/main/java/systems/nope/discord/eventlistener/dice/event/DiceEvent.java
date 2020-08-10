package systems.nope.discord.eventlistener.dice.event;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public abstract class DiceEvent {
    private final MessageReceivedEvent event;
    boolean handled = false;

    public DiceEvent(MessageReceivedEvent event) {
        this.event = event;
    }

    public void handle() {
        handled = true;
        removeMessage();
        sendMessage(this.toString());
    }

    /**
     * sends the given message to the requesting channel
     *
     * @param message - String to send
     */
    void sendMessage(String message) {
        event.getChannel().sendMessage(message)
                .queue(response -> {
                            response.editMessageFormat(message).queue();
                        }
                );
    }

    /**
     * removes the message which invoked the command
     */
    private void removeMessage() {
        if (event.getChannelType() != ChannelType.PRIVATE)
            try {
                event.getMessage().delete().complete();
            } catch (InsufficientPermissionException e) {
                System.out.println("could not delete user message.");
            } catch (ErrorResponseException e) {
                System.out.println("ERROR: " + e.getMeaning() + "\nMESSAGE: " + e.getMessage());
            }
    }

    public Member getAuthor() {
        return event.getMember();
    }

    public String getAuthorName() {
        if (event.getMember().getNickname() != null)
            return event.getMember().getNickname();
        else
            return event.getAuthor().getName();
    }
}
