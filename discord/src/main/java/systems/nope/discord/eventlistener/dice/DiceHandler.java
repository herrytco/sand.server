package systems.nope.discord.eventlistener.dice;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import systems.nope.discord.util.StringUtil;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DiceHandler extends ListenerAdapter {
    private static final SecureRandom rng = new SecureRandom();

    private Map<User, Integer> nTries = new HashMap<>();
    private Map<User, Integer> sum = new HashMap<>();
    private Map<User, Integer> diceTypes = new HashMap<>();
    private Map<User, Integer> difficulties = new HashMap<>();

    private String getEmojiForResult(int result) {
        switch (result) {
            case 1:
                return ServerConstants.emoteD20Result1;
            case 20:
                return ServerConstants.emoteD20Result20;
            default:
                return ServerConstants.emoteD20Result2To19;
        }
    }

    private int rollOnce(User author) {
        int bound = 20;

        if (diceTypes.containsKey(author))
            bound = diceTypes.get(author);

        int result = rng.nextInt(bound) + 1;

        if (!nTries.containsKey(author)) {
            nTries.put(author, 0);
            sum.put(author, 0);
        }

        nTries.put(author, nTries.get(author) + 1);
        sum.put(author, sum.get(author) + result);

        return result;
    }

    private String getCalculation(User author, int result) {
        if (!difficulties.containsKey(author) || difficulties.get(author) == 0)
            return String.format(ServerConstants.singleResult, getEmojiForResult(result), result);

        String sign = difficulties.get(author) < 0 ? "-" : "+";

        return String.format(
                ServerConstants.calculation,
                getEmojiForResult(result),
                result,
                sign,
                Math.abs(difficulties.get(author)),
                result + difficulties.get(author)
        );
    }

    private String getSingleRoll(MessageReceivedEvent event, int k) {
        String message;

        User author = event.getMessage().getAuthor();

        if (event.getChannelType() == ChannelType.PRIVATE)
            message = "Between us? You rolled a marvellous 20!";
        else {
            int result = rollOnce(author);

            if (k < 0)
                message = String.format(ServerConstants.regularRoll, event.getAuthor().getName(), getCalculation(author, result));
            else
                message = String.format(ServerConstants.kthRoll, event.getAuthor().getName(), StringUtil.integerToOrderedString(k), getCalculation(author, result));
        }

        return message;
    }

    /**
     * rolls a single d20 dice and prints the result in the chat
     *
     * @param event - message which triggered the command - gets deleted in the process
     */
    private void regularRoll(MessageReceivedEvent event) {
        String message = getSingleRoll(event, -1);

        removeMessage(event);

        sendMessage(event, message);
    }

    /**
     * rolles x dice and prints the respective result in the console
     *
     * @param event - message which triggered the command - gets deleted in the process
     * @param x     - number of rolls
     */
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
        sendMessage(event, message);
    }

    private void xRollExtreme(MessageReceivedEvent event, int x, boolean maximum) {
        removeMessage(event);

        StringBuilder sb = new StringBuilder();

        int[] rolls = new int[x];

        rolls[0] = rollOnce(event.getAuthor());
        int result = rolls[0];

        for (int i = 1; i < x; i++) {
            int r = rollOnce(event.getAuthor());
            rolls[i] = r;

            if (maximum && r > result)
                result = r;

            if (!maximum && r < result)
                result = r;
        }

        StringBuilder all = new StringBuilder("(");

        for (int i = 0; i < rolls.length; i++) {
            all.append(getCalculation(event.getAuthor(), rolls[i]));

            if (i < rolls.length - 1)
                all.append(", ");
        }
        all.append(")");

        sendMessage(event, String.format(
                "%s rolled %d dice, the %s result is %s!\nAll rolls were:%s",
                event.getAuthor().getName(),
                x,
                maximum ? "best" : "worst",
                getCalculation(event.getAuthor(), result),
                all.toString()
        ));
    }

    /**
     * rolles x dice and calculates the cumulative result, which gets printed in the channel
     *
     * @param event - message which triggered the command - gets deleted in the process
     * @param x     - number of rolls
     */
    private void cRolls(MessageReceivedEvent event, int x) {
        removeMessage(event);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s rolls %d culative dice:\n", event.getAuthor().getName(), x));
        int resultCumulative = 0;

        for (int i = 0; i < x; i++) {
            int singleResult = rollOnce(event.getAuthor());

            resultCumulative += singleResult;
            sb.append(String.format("%s's %s dice: %s\n", event.getAuthor().getName(), StringUtil.integerToOrderedString(i + 1), getCalculation(event.getAuthor(), singleResult)));
        }

        if (difficulties.containsKey(event.getAuthor()))
            resultCumulative += difficulties.get(event.getAuthor()) * x;

        sb.append(String.format("The total sum is ... *furiously typing on calculator* ... %d!", resultCumulative));

        String message = sb.toString();

        sendMessage(event, message);
    }

    /**
     * @param event - message which should get deleted
     */
    private void removeMessage(MessageReceivedEvent event) {
        if (event.getChannelType() != ChannelType.PRIVATE)
            try {
                event.getMessage().delete().complete();
            } catch (InsufficientPermissionException e) {
                System.out.println("could not delete user message.");
            }
    }

    /**
     * calculates the average of a user and prints it to the channel
     *
     * @param event - message which should get deleted
     */
    private void avg(MessageReceivedEvent event) {
        String message;
        User author = event.getAuthor();

        float avg = avgOfUser(author);

        if (avg >= 0)
            message = String.format("%s' average: <:d20rng:433634881118142465> %.2f", author.getName(), avg);
        else
            message = String.format("%s should do some rolls first before bossing me around...", author.getName());

        removeMessage(event);
        sendMessage(event, message);
    }

    /**
     * calculates the server-average of all users and prints it to the channel of the event
     *
     * @param event - message which should get deleted
     */
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
        sendMessage(event, message);
    }


    /**
     * resets the server average
     *
     * @param event - message which should get deleted
     */
    private void savgreset(MessageReceivedEvent event) {
        nTries = new HashMap<>();
        sum = new HashMap<>();

        String message = "I forgot everything each of you did ... Who are you guys again?";
        sendMessage(event, message);
    }

    /**
     * resets the average of a single user
     *
     * @param event - message which should get deleted
     */
    private void reset(MessageReceivedEvent event) {
        User author = event.getAuthor();

        nTries.remove(author);
        sum.remove(author);

        removeMessage(event);

        String message = String.format("I forgot everything what %s rolled.", author.getName());
        sendMessage(event, message);
    }

    /**
     * sends the given message to the requesting channel
     *
     * @param event   - message which is replied to
     * @param message - String to send
     */
    private void sendMessage(MessageReceivedEvent event, String message) {
        event.getChannel().sendMessage(message)
                .queue(response -> {
                            response.editMessageFormat(message).queue();
                        }
                );
    }

    /**
     * calculates the average roll score for the given user
     *
     * @param user - subject of the calculation
     * @return average roll score || -1 if no data is stored
     */
    private float avgOfUser(User user) {
        if (nTries.containsKey(user))
            return (float) sum.get(user) / nTries.get(user);
        else
            return -1;
    }

    private boolean handleSingleCommands(@Nonnull MessageReceivedEvent event, String command) {
        switch (command) {
            case "!roll":
                regularRoll(event);
                return true;
            case "!uavg":
                avg(event);
                return true;
            case "!uavgreset":
                reset(event);
                return true;
            case "!savg":
                savg(event);
                return true;
            case "!savgreset":
                savgreset(event);
                return true;
            case "!help":
                removeMessage(event);
                sendMessage(event, ServerConstants.help);
                return true;
            default:
                return false;
        }
    }

    private boolean handleMultiCommands(@Nonnull MessageReceivedEvent event, String... command) {
        if (command.length > 0) {
            switch (command[0]) {
                case "!umod":
                    try {
                        removeMessage(event);
                        int x = Integer.parseInt(command[1]);
                        difficulties.put(event.getAuthor(), x);
                        sendMessage(event, String.format("%s, has now a difficulty of %d.", event.getAuthor().getName(), x));
                    } catch (NumberFormatException e) {
                        sendMessage(event, String.format("%s, '%s' is not a number sir.", event.getAuthor().getName(), command[1]));
                    }
                    return true;
                case "!udice":
                    if (command.length == 2) {
                        try {
                            removeMessage(event);
                            int x = Integer.parseInt(command[1]);
                            diceTypes.put(event.getAuthor(), x);
                            sendMessage(event, String.format("%s, now uses a %d-sided dice.", event.getAuthor().getName(), x));
                        } catch (NumberFormatException e) {
                            sendMessage(event, String.format("Roll your '%s'-sided dice yourself, sir.", command[1]));
                        }
                    }
                    return true;
                case "!rollx":
                    if (command.length >= 2)
                        try {
                            int x = Integer.parseInt(command[1]);

                            if (x > 0 && x < 50) {
                                if (command.length == 2)
                                    xRolls(event, x);
                                else if (command.length == 3) {
                                    switch (command[2]) {
                                        case "min":
                                            xRollExtreme(event, x, false);
                                            break;
                                        case "max":
                                            xRollExtreme(event, x, true);
                                            break;
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            sendMessage(event, String.format("Please give me a detailed explanation about how I am able to roll a dice '%s' times.", command[1]));
                        }
                    return true;

                case "!rollc":
                    if (command.length >= 2)
                        try {
                            int x = Integer.parseInt(command[1]);
                            cRolls(event, x);
                        } catch (NumberFormatException e) {
                            sendMessage(event, String.format("Please give me a detailed explanation about how I am able to roll a dice '%s' times.", command[1]));
                        }
                    return true;
                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (!handleSingleCommands(event, message) && !handleMultiCommands(event, message.split(" "))) {

        }
    }
}
