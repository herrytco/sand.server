package systems.nope.discordintegration.eventhandlers;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.security.SecureRandom;

public class DiceMessageHandler extends ListenerAdapter {
    private static final SecureRandom srng = new SecureRandom();

    private String getEmojiForResult(int result) {
        switch (result) {
            case 1:
                return ":nat1:";
            case 20:
                return ":nat20:";
            default:
                return ":d20:";
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        System.out.println(String.format("Received Message %s", event.getMessage().getContentRaw()));
        if (event.getMessage().getContentRaw().startsWith("!roll")) {
            System.out.println("It is a dice-rolling command, ho nice!");
            String message;

            if (event.isFromType(ChannelType.PRIVATE)) {
                message = "Between us? You rolled a marvellous 20!";
            } else {
                int result = srng.nextInt(20) + 1;
                message = String.format("%s rolled a %s %d", event.getAuthor().getName(), getEmojiForResult(result), result);
            }

            event.getChannel().sendMessage(message) /* => RestAction<Message> */
                    .queue(response /* => Message */ -> {
                        response.editMessageFormat(message).queue();
                    });
        }
    }
}
