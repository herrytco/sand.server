package systems.nope.discord.eventlistener;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.security.SecureRandom;

public class DiceHandler extends ListenerAdapter {
    private static final SecureRandom rng = new SecureRandom();

    private String getEmojiForResult(int result) {
        switch (result) {
            case 1:
                return "<:nat1:433634880774078465>";
            case 20:
                return "<:nat20:433634880656506890>";
            default:
                return "<:d20unsure:433634881063616512>";
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals("!roll")) {
            String message;
            try {
                event.getMessage().delete().complete();
            } catch (InsufficientPermissionException e) {
                System.out.println("could not delete user message.");
            }

            if (event.getChannelType() == ChannelType.PRIVATE)
                message = "Between us? You rolled a marvellous 20!";
            else {
                int result = rng.nextInt(20) + 1;
                message = String.format("%s rolled a %s %d", event.getAuthor().getName(), getEmojiForResult(result), result);
            }

            event.getChannel().sendMessage(message) /* => RestAction<Message> */
                    .queue(response /* => Message */ -> {
                        response.editMessageFormat(message).queue();
                    });
        }
    }
}
