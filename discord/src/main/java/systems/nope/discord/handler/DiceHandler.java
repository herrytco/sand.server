package systems.nope.discord.handler;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import systems.nope.discord.exceptions.ParseException;
import systems.nope.discord.util.CommandUtil;
import systems.nope.discord.util.DiscordUtil;
import systems.nope.discord.event.*;

import javax.annotation.Nonnull;
import java.util.*;

public class DiceHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        message = message.replaceAll("\\s+", " ");

        if (event.getChannelType() == ChannelType.TEXT) {
            System.out.printf("%s> %s\n", DiscordUtil.getMemberName(event.getMember()), message);

            DiceEvent e;
            try {
                e = CommandUtil.parseCommand(message, event);

                if (e != null)
                    e.handle();
            } catch (ParseException parseException) {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage(parseException.getErrorMessage()).queue();
            }
        }
    }

    private Optional<Integer> parseInt(String numberString, MessageReceivedEvent event) {
        return parseInt(numberString, event, null);
    }

    private Optional<Integer> parseInt(String numberString, MessageReceivedEvent event, String errorMessage) {
        try {
            return Optional.of(Integer.parseInt(numberString));
        } catch (NumberFormatException e) {
            if (errorMessage != null)
                DiscordUtil.sendMessage(event, errorMessage);
            return Optional.empty();
        }
    }


}
