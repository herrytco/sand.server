package systems.nope.discord.util;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class DiscordUtil {

    /**
     * @param event - message which should get deleted
     */
    public static void removeMessage(MessageReceivedEvent event) {
        if (event.getChannelType() != ChannelType.PRIVATE)
            try {
                event.getMessage().delete().complete();
            } catch (InsufficientPermissionException e) {
                System.out.println("Could not delete user message.");
            }
    }

    /**
     * sends the given message to the requesting channel
     *
     * @param event   - message which is replied to
     * @param message - String to send
     */
    public static void sendMessage(MessageReceivedEvent event, String message) {
        event.getChannel().sendMessage(message)
                .queue(response -> {
                            response.editMessageFormat(message).queue();
                        }
                );
    }

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
