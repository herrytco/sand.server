package systems.nope.discord.eventlistener;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class DiceHandler extends ListenerAdapter {
    private static final SecureRandom rng = new SecureRandom();

    private Map<User, Integer> nTries = new HashMap<>();
    private Map<User, Integer> sum = new HashMap<>();

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

    private String getOrderNumber(int k) {
        switch (k) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            default:
                return String.format("%sth", k);
        }
    }

    private String getSingleRoll(MessageReceivedEvent event, int k) {
        String message;

        User author = event.getMessage().getAuthor();

        if (event.getChannelType() == ChannelType.PRIVATE)
            message = "Between us? You rolled a marvellous 20!";
        else {
            int result = rng.nextInt(20) + 1;


            if (!nTries.containsKey(author)) {
                nTries.put(author, 0);
                sum.put(author, 0);
            }

            nTries.put(author, nTries.get(author) + 1);
            sum.put(author, sum.get(author) + result);

            if (k < 0)
                message = String.format("%s rolled a %s %d", event.getAuthor().getName(), getEmojiForResult(result), result);
            else
                message = String.format("%s's %s dice: %s %d", event.getAuthor().getName(), getOrderNumber(k), getEmojiForResult(result), result);
        }

        return message;
    }

    private void regularRoll(MessageReceivedEvent event) {
        String message = getSingleRoll(event, -1);

        removeMessage(event);

        event.getChannel().sendMessage(message)
                .queue(response -> {
                            response.editMessageFormat(message).queue();
                        }
                );
    }

    private void xRolls(MessageReceivedEvent event, int x) {
        removeMessage(event);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s rolls %d dice:\n", event.getAuthor().getName(), x));

        for (int i = 0; i < x; i++) {
            sb.append(getSingleRoll(event, i + 1));
            if (i < x - 1)
                sb.append("\n");
        }

        String message = sb.toString();

        event.getChannel().sendMessage(message)
                .queue(response -> {
                            response.editMessageFormat(message).queue();
                        }
                );
    }

    private void removeMessage(MessageReceivedEvent event) {
        if (event.getChannelType() != ChannelType.PRIVATE)
            try {
                event.getMessage().delete().complete();
            } catch (InsufficientPermissionException e) {
                System.out.println("could not delete user message.");
            }
    }

    private float avgOfUser(User user) {
        if (nTries.containsKey(user))
            return (float) sum.get(user) / nTries.get(user);
        else
            return -1;
    }

    private void avg(MessageReceivedEvent event) {
        String message;
        User author = event.getAuthor();

        float avg = avgOfUser(author);

        if (avg >= 0)
            message = String.format("%s' average: <:d20rng:433634881118142465> %.2f", author.getName(), avg);
        else
            message = String.format("%s should do some rolls first before bossing me around...", author.getName());

        removeMessage(event);
        event.getChannel().

                sendMessage(message)
                .

                        queue(response ->

                                {
                                    response.editMessageFormat(message).queue();
                                }
                        );
    }

    private void savg(MessageReceivedEvent event) {
        float avg = 0;
        int k = 0;

        for (User u : nTries.keySet()) {
            avg += avgOfUser(u);
            k++;
        }

        if (k > 0)
            avg /= k;

        String message;
        if (k > 0)
            message = String.format("Server average: <:d20rng:433634881118142465> %.2f", avg);
        else
            message = "Boys, if you want some averages you have to ROLL to get empirical data to average!";

        removeMessage(event);
        event.getChannel().sendMessage(message)
                .queue(response -> {
                            response.editMessageFormat(message).queue();
                        }
                );
    }

    private void savgreset(MessageReceivedEvent event) {
        nTries = new HashMap<>();
        sum = new HashMap<>();

        String message = "I forgot everything each of you did ... Who are you guys again?";
        event.getChannel().sendMessage(message)
                .queue(response -> {
                            response.editMessageFormat(message).queue();
                        }
                );
    }

    private void reset(MessageReceivedEvent event) {
        User author = event.getAuthor();

        nTries.remove(author);
        sum.remove(author);

        removeMessage(event);

        String message = String.format("I forgot everything what %s rolled.", author.getName());
        event.getChannel().sendMessage(message)
                .queue(response -> {
                            response.editMessageFormat(message).queue();
                        }
                );
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.equals("!roll")) {
            regularRoll(event);
        } else if (message.startsWith("!rollx")) {
            String[] parts = message.split(" ");
            try {
                int x = Integer.parseInt(parts[1]);
                xRolls(event, x);
            } catch (NumberFormatException e) {
                System.out.println("Weird number given ...");
            }
        } else if (message.equals("!uavg"))
            avg(event);
        else if (message.equals("!uavgreset"))
            reset(event);
        else if (message.equals("!savg"))
            savg(event);
        else if (message.equals("!savgreset"))
            savgreset(event);
    }
}
