package systems.nope.discord.eventlistener.dice;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import systems.nope.discord.util.StringUtil;

import javax.annotation.Nonnull;
import java.nio.channels.Channel;
import java.security.SecureRandom;
import java.util.*;

public class DiceHandler extends ListenerAdapter {
    private static final SecureRandom rng = new SecureRandom();

    private Map<Member, Integer> nTries = new HashMap<>();
    private Map<Member, Integer> sum = new HashMap<>();
    private Map<Member, Integer> diceTypes = new HashMap<>();
    private Map<Member, Integer> difficulties = new HashMap<>();
    private Map<Member, Party> parties = new HashMap<>();



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

    private int rollOnce(Member author) {
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

    private String getCalculation(Member author, int result) {
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

        Member author = event.getMessage().getMember();

        if (event.getChannelType() == ChannelType.PRIVATE)
            message = "Between us? You rolled a marvellous 20!";
        else {
            int result = rollOnce(author);

            if (k < 0)
                if (event.getMember().getNickname() != null)
                    message = String.format(ServerConstants.regularRoll, event.getMember().getNickname(), getCalculation(author, result));
                else
                    message = String.format(ServerConstants.regularRoll, event.getMember().getUser().getName(), getCalculation(author, result));
            else
                if (event.getMember().getNickname() != null)
                    message = String.format(ServerConstants.kthRoll, event.getMember().getNickname(), StringUtil.integerToOrderedString(k), getCalculation(author, result));
                else
                    message = String.format(ServerConstants.kthRoll, event.getMember().getUser().getName(), StringUtil.integerToOrderedString(k), getCalculation(author, result));
        }

        return message;
    }


    private String getSingleRoll(MessageReceivedEvent event, int k, String name) {
        String message;

        Member author = event.getMessage().getMember();

        if (event.getChannelType() == ChannelType.PRIVATE)
            message = "Between us? You rolled a marvellous 20!";
        else {
            int result = rollOnce(author);

            if (k < 0)
                message = String.format(ServerConstants.regularRoll, name, getCalculation(author, result));
            else
                message = String.format(ServerConstants.kthRoll, name, StringUtil.integerToOrderedString(k), getCalculation(author, result));
        }

        return message;
    }

    private String getSingleRoll(MessageReceivedEvent event, String name) {
        String message;

        Member author = event.getMessage().getMember();

        if (event.getChannelType() == ChannelType.PRIVATE)
            message = "Between us? You rolled a marvellous 20!";
        else {

            int result = rollOnce(author);
            message = String.format(ServerConstants.regularRoll, name, getCalculation(author, result));
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
     * rolls x dice and prints the respective result in the console
     *
     * @param event - message which triggered the command - gets deleted in the process
     * @param x     - number of rolls
     */
    private void xRolls(MessageReceivedEvent event, int x) {
        removeMessage(event);

        StringBuilder sb = new StringBuilder();
        if (event.getMember().getNickname() != null)
            sb.append(String.format("%s rolls %d dice:\n", event.getMember().getNickname(), x));
        else
            sb.append(String.format("%s rolls %d dice:\n", event.getMember().getUser().getName(), x));

        for (int i = 0; i < x; i++) {
            sb.append(getSingleRoll(event, i + 1));
            if (i < x - 1)
                sb.append("\n");
        }

        String message = sb.toString();
        sendMessage(event, message);
    }

    private void xRolls(MessageReceivedEvent event, int x, String name) {
        removeMessage(event);

        StringBuilder sb = new StringBuilder();

            sb.append(String.format("%s rolls %d dice:\n", name, x));

        for (int i = 0; i < x; i++) {
            sb.append(getSingleRoll(event, i + 1, name));
            if (i < x - 1)
                sb.append("\n");
        }

        String message = sb.toString();
        sendMessage(event, message);
    }

    private String singleXRolls(MessageReceivedEvent event, String name, int nr) { //in your area
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append(String.format("%s's rolls:\n", name));//header

        for (int i = 0; i < nr; i++) {
            messageBuilder.append(getSingleRoll(event, i + 1, name));
            messageBuilder.append("\n");
        }

        String message = messageBuilder.toString();
        return message;
    }

    private String singleTRolls(MessageReceivedEvent event, String name, int nr) { //in your area
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append(String.format("%s is targeted by %d dice:\n", name, nr));//header

        for (int i = 0; i < nr; i++) {
            messageBuilder.append(getSingleRoll(event, i + 1, name));
            messageBuilder.append("\n");
        }

        String message = messageBuilder.toString();
        return message;
    }

    private void allTRolls(MessageReceivedEvent event, String[] command){

        StringBuilder messageBuilder = new StringBuilder();//Final message is build by this

        //Iterate over names
        for (int i = 1; i < command.length; i = i + 2){ //every odd number in command is a name

            messageBuilder.append("\n");//does not get shown at the start of a message sadly
            //try converting nr to int
            try {
                //Roll [nr] times
                messageBuilder.append(singleTRolls(event, command[i], Integer.parseInt(command[i + 1])));

            } catch (NumberFormatException e) {
                sendMessage(event, String.format("Please give me a detailed explanation about how I am able to roll a dice '%s' times.", command[i+1]));
            }
        }

        removeMessage(event);//We have to delete at the end so we can roll more than once
        String message = messageBuilder.toString();
        sendMessage(event, message);
    }

    private void xRollExtreme(MessageReceivedEvent event, int x, boolean maximum) {
        removeMessage(event);

        StringBuilder sb = new StringBuilder();

        int[] rolls = new int[x];

        rolls[0] = rollOnce(event.getMember());
        int result = rolls[0];

        for (int i = 1; i < x; i++) {
            int r = rollOnce(event.getMember());
            rolls[i] = r;

            if (maximum && r > result)
                result = r;

            if (!maximum && r < result)
                result = r;
        }

        StringBuilder all = new StringBuilder("(");

        for (int i = 0; i < rolls.length; i++) {
            all.append(getCalculation(event.getMember(), rolls[i]));

            if (i < rolls.length - 1)
                all.append(", ");
        }
        all.append(")");

        if (event.getMember().getNickname() != null)
            sendMessage(event, String.format(
                    "%s rolled %d dice, the %s result is %s!\nAll rolls were:%s",
                    event.getMember().getNickname(),
                    x,
                    maximum ? "best" : "worst",
                    getCalculation(event.getMember(), result),
                    all.toString()
            ));
        else
            sendMessage(event, String.format(
                    "%s rolled %d dice, the %s result is %s!\nAll rolls were:%s",
                    event.getMember().getUser().getName(),
                    x,
                    maximum ? "best" : "worst",
                    getCalculation(event.getMember(), result),
                    all.toString()
            ));
    }

    /**
     * rolls x dice and calculates the cumulative result, which gets printed in the channel
     *
     * @param event - message which triggered the command - gets deleted in the process
     * @param x     - number of rolls
     */
    private void cRolls(MessageReceivedEvent event, int x) {
        removeMessage(event);

        StringBuilder sb = new StringBuilder();

        if (event.getMember().getNickname() != null)
            sb.append(String.format("%s rolls %d culative dice:\n", event.getMember().getNickname(), x));
        else
            sb.append(String.format("%s rolls %d culative dice:\n", event.getMember().getUser().getName(), x));
        int resultCumulative = 0;

        for (int i = 0; i < x; i++) {
            int singleResult = rollOnce(event.getMember());

            resultCumulative += singleResult;

            if (event.getMember().getNickname() != null)
                sb.append(String.format("%s's %s dice: %s\n", event.getMember().getNickname(), StringUtil.integerToOrderedString(i + 1), getCalculation(event.getMember(), singleResult)));
            else
                sb.append(String.format("%s's %s dice: %s\n", event.getMember().getUser().getName(), StringUtil.integerToOrderedString(i + 1), getCalculation(event.getMember(), singleResult)));
        }

        if (difficulties.containsKey(event.getMember()))
            resultCumulative += difficulties.get(event.getMember()) * x;

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
        Member author = event.getMember();

        float avg = avgOfMember(author);

        if (avg >= 0)

            if (event.getMember().getNickname() != null)
                message = String.format("%s' average: <:d20rng:433634881118142465> %.2f", author.getNickname(), avg);
            else
                message = String.format("%s' average: <:d20rng:433634881118142465> %.2f", author.getUser().getName(), avg);
        else
            if (event.getMember().getNickname() != null)
                message = String.format("%s should do some rolls first before bossing me around...", author.getNickname());
            else
                message = String.format("%s should do some rolls first before bossing me around...", author.getUser().getName());

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

        for (Member u : nTries.keySet()) {
            avg += avgOfMember(u);
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
        Member author = event.getMember();

        nTries.remove(author);
        sum.remove(author);

        removeMessage(event);

        String message;
        if (author.getNickname() != null)
            message = String.format("I forgot everything what %s rolled.", author.getNickname());
        else
            message = String.format("I forgot everything what %s rolled.", author.getUser().getName());

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
     * calculates the average roll score for the given member
     *
     * @param member - subject of the calculation
     * @return average roll score || -1 if no data is stored
     */
    private float avgOfMember(Member member) {
        if (nTries.containsKey(member))
            return (float) sum.get(member) / nTries.get(member);
        else
            return -1;
    }

    private void createParty(MessageReceivedEvent event){
        //Create party and add it to parties
        Party party = new Party();
        party.leader = event.getMember();
        party.members = new ArrayList<Member>(event.getMember().getVoiceState().getChannel().getMembers()); //Cast to list so we can mutate it

        StringBuilder messageBuilder = new StringBuilder();

        int memberCount;
        for (memberCount = 0; memberCount < party.members.size(); memberCount++){ //foreach party member

            if (party.members.get(memberCount).getUser().isBot()){ //bots are not fun to party with

                party.members.remove(memberCount);
                memberCount--; //if this was not here when memberCount will be incremented we would skip one member
            }
            else if (party.members.get(memberCount).equals(event.getMember())) { //hosts also ruin the fun

                party.members.remove(memberCount);
                memberCount--; //if this was not here when memberCount will be incremented we would skip one member
            }
            else {

                if (memberCount != 0)
                    messageBuilder.append(", "); //Seperate party members after the first one
                messageBuilder.append(party.members.get(memberCount).getUser().getName());
            }
        }

        if (memberCount == 0){
            sendMessage(event, "There is no one to party with T-T");
        }
        else if (memberCount == 1){

            messageBuilder.append(" is now partying\uD83C\uDF89");
        }
        else {

            messageBuilder.append(" are now partying\uD83C\uDF89");
        }

        parties.put(party.leader, party);

        sendMessage(event, messageBuilder.toString());
        removeMessage(event);
    }

    private boolean handleSingleCommands(@Nonnull MessageReceivedEvent event, String command) {
        switch (command) {
            case "!roll":
                regularRoll(event);
                return true;

            case "!proll":
                if (parties.get(event.getMember()) != null && parties.get(event.getMember()).members != null) {//check if author is partyleader

                    //get partyMembers for readability
                    List<Member> partyMembers = parties.get(event.getMember()).members;

                    if (partyMembers.size() != 0) { //at least one member

                        StringBuilder messageBuilder = new StringBuilder();
                        messageBuilder.append("Rolling once for each party member:\n");

                        //iterate over partyMembers
                        for (Member member : partyMembers) {

                            if (member.getNickname() != null)
                                messageBuilder.append(getSingleRoll(event, member.getNickname()));
                            else
                                messageBuilder.append(getSingleRoll(event, member.getUser().getName()));
                            messageBuilder.append('\n');
                        }
                        sendMessage(event, messageBuilder.toString());
                        removeMessage(event);
                    } else {
                        sendMessage(event, "This party is boring! I wont perform for it.");
                        removeMessage(event);
                    }
                }
                else {
                    sendMessage(event, "Maybe organize a !party first.");
                    removeMessage(event);
                }

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

            case "!party":
                createParty(event);
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
                    if (command.length == 2) {
                        try {
                            removeMessage(event);
                            int x = Integer.parseInt(command[1]);
                            difficulties.put(event.getMember(), x);

                            if (event.getMember().getNickname() != null)
                                sendMessage(event, String.format("%s, has now a difficulty of %d.", event.getMember().getNickname(), x));
                            else
                                sendMessage(event, String.format("%s, has now a difficulty of %d.", event.getMember().getUser().getName(), x));
                        } catch (NumberFormatException e) {

                            if (event.getMember().getNickname() != null)
                                sendMessage(event, String.format("%s, '%s' is not a number sir.", event.getMember().getNickname(), command[1]));
                            else
                                sendMessage(event, String.format("%s, '%s' is not a number sir.", event.getMember().getUser().getName(), command[1]));
                        }
                        return true;
                    }
                    else if (command.length > 2) {
                        sendMessage(event, "If you want two modifiers, just add them together. I'm not your buttler or something");
                        return true;
                    }
                    else {
                        sendMessage(event, "I think you forgot to tell me something.");
                        return true;
                    }



                case "!udice":
                    if (command.length == 2) {
                        try {
                            removeMessage(event);
                            int x = Integer.parseInt(command[1]);
                            diceTypes.put(event.getMember(), x);

                            if (event.getMember().getNickname() != null)
                                sendMessage(event, String.format("%s, now uses a %d-sided dice.", event.getMember().getNickname(), x));
                            else
                                sendMessage(event, String.format("%s, now uses a %d-sided dice.", event.getMember().getUser().getName(), x));
                        } catch (NumberFormatException e) {
                            sendMessage(event, String.format("Roll your '%s'-sided dice yourself, sir.", command[1]));
                        }
                        return true;
                    }
                    else if (command.length == 1){
                        sendMessage(event, "If you don't want to throw a die, just don't!");
                        return true;
                    }
                    else {
                        sendMessage(event, "Sorry, but you'll have to decide how many sides you want.");
                        return true;
                    }


                case "!prollx":
                    if (command.length >= 2) {
                        try {

                            StringBuilder messageBuilder = new StringBuilder();

                            messageBuilder.append(String.format("Everybody in the party\uD83C\uDF89 rolls %d dice\n", Integer.parseInt(command[1])));

                            if (parties.get(event.getMember()) != null) { //Check if member is party leader

                                Party party = parties.get(event.getMember());
                                for (Member member : party.members) {
                                    messageBuilder.append(singleXRolls(event, member.getEffectiveName(), Integer.parseInt(command[1])));
                                }
                                sendMessage(event, messageBuilder.toString());
                                removeMessage(event);
                                return  true;
                            } else {
                                sendMessage(event, "Maybe try organizing a !party first.");
                                return  true;
                            }
                        }
                        catch (NumberFormatException e) {
                            sendMessage(event, String.format("Please give me a detailed description on how i'm able to roll %s dice", command[1]));
                            return  true;
                        }
                    }
                    else {
                        sendMessage(event, "I think you forgot to tell me something.");
                        return  true;
                    }


                case "!rollx":
                    if (command.length >= 2) {
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
                            return true;
                        }
                    }
                    else {
                        sendMessage(event, "I think you forgot to tell me something.");
                    }
                    return true;


                case "!rollt":
                    //Syntax: !rollt [target] [nr] [tame] [nr] ...
                    //so command.length has to be uneven and not one
                    if ((command.length % 2) == 1 && command.length != 1) {
                        allTRolls(event, command);
                    }
                    else if (command.length == 1) {
                        sendMessage(event, "I think you forgot to tell me something.");
                    }
                    else {
                        sendMessage(event, "I need a name and a number corresponding to it.");
                    }
                    return true;


                case "!rollc":

                    if (command.length >= 2) {
                        try {
                            int x = Integer.parseInt(command[1]);
                            cRolls(event, x);
                        } catch (NumberFormatException e) {
                            sendMessage(event, String.format("Please give me a detailed explanation about how I am able to roll a dice '%s' times.", command[1]));
                        }
                    }
                    else {
                        sendMessage(event, "I think you forgot to tell me something.");
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
