package systems.nope.discord.eventlistener.dice;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import systems.nope.discord.eventlistener.dice.event.*;
import systems.nope.discord.eventlistener.dice.party.event.*;
import systems.nope.discord.eventlistener.dice.person.event.*;

import javax.annotation.Nonnull;
import java.util.*;

public class DiceHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        message = message.replaceAll("\\s+", " ");

        if (event.getChannelType() == ChannelType.GROUP &&
                (handleSingleCommands(event, message) || handleMultiCommands(event, message.split(" ")))
        ) {
            System.out.println("> " + message);
        }
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
     * handles every command that does NOT use arguments
     *
     * @param event   - message event
     * @param command - entered command
     * @return whether the event was handled or not
     */
    private boolean handleSingleCommands(@Nonnull MessageReceivedEvent event, String command) {
        DiceEvent de;

        switch (command) {
            case "!link":
                de = new RelinkEvent(event);
                break;

            case "!roll":
                de = new RollEvent(event);
                break;

            case "!umod":
                de = new PrintDiceModifierEvent(event);
                break;

            case "!uavg":
                de = new AverageEvent(event, AverageScope.User);
                break;

            case "!savg":
                de = new AverageEvent(event, AverageScope.Server);
                break;

            case "!uavgreset":
                de = new AverageResetEvent(event, AverageScope.User);
                break;

            case "!savgreset":
                de = new AverageResetEvent(event, AverageScope.Server);
                break;

            case "!help":
                de = new HelpEvent(event);
                break;

            case "!unlink":
                de = new UnlinkEvent(event);
                break;

            case "!clink":
                de = new CheckLinkEvent(event);
                break;

            case "!party":
                de = new CreatePartyEvent(event);
                break;

            case "!proll":
                de = new PartyRollEvent(event);
                break;

            default:
                return false;
        }

        de.handle();
        return true;
    }

    private Optional<Integer> parseInt(String numberString, MessageReceivedEvent event) {
        return parseInt(numberString, event, null);
    }

    private Optional<Integer> parseInt(String numberString, MessageReceivedEvent event, String errorMessage) {
        try {
            return Optional.of(Integer.parseInt(numberString));
        } catch (NumberFormatException e) {
            if (errorMessage != null)
                sendMessage(event, errorMessage);
            return Optional.empty();
        }
    }

    private boolean handleMultiCommands(@Nonnull MessageReceivedEvent event, String... command) {
        if (command.length > 0) {
            DiceEvent de = null;

            switch (command[0]) {
                case "!proll":
                    if (command.length == 2) {
                        Optional<Integer> modifier = parseInt(command[1], event, "The modifier has to be a whole number. Like 1, 2 or 3 ... you learn that in elementary school usually.");

                        if (modifier.isPresent())
                            de = new ModifiedPartyRollEvent(event, modifier.get());
                    }
                    break;

                case "!roll":
                    if (command.length == 2) {
                        int x = Integer.parseInt(command[1]);

                        de = new ModifiedRollEvent(event, x);
                    }
                    break;

                case "!rollx":
                    if (command.length >= 2) {
                        Optional<Integer> number = parseInt(
                                command[1],
                                event,
                                String.format("Please give me a detailed explanation about how I am able to roll a dice '%s' times.", command[1])
                        );

                        if (number.isEmpty())
                            return true;

                        int x = number.get();

                        if (x < 1) {
                            sendMessage(event, String.format("The number of throws must be positive ... so please roll your %d dice yourself!", x));
                            return true;
                        }

                        if (x > 50) {
                            sendMessage(event, "I am quite limited by the technology of my time, 50 rolls is the maximum sir.");
                            return true;
                        }

                        if (command.length == 2) {
                            de = new RollXEvent(event, x);
                            break;
                        }

                        if (command.length == 3) {
                            Optional<Integer> thirdParameter = parseInt(command[2], event);

                            // third argument is actually a number
                            if (thirdParameter.isPresent()) {
                                de = new ModifiedRollXEvent(event, x, thirdParameter.get());
                                break;
                            }

                            if (command[2].equals("min") || command[2].equals("max")) {
                                de = new RollXExtremumEvent(event, x, command[2].equals("max"));
                                break;
                            }
                        }

                        if (command.length == 4) {
                            Optional<Integer> thirdParameter = parseInt(command[2], event);
                            Optional<Integer> fourthParameter = parseInt(command[3], event);

                            if (thirdParameter.isEmpty() && (command[2].equals("min") || command[2].equals("max"))) {
                                // weird IDE bug, had to move the if down here ¯\_(ツ)_/¯
                                if (fourthParameter.isPresent()) {
                                    de = new ModifiedRollXExtremumEvent(event, x, command[2].equals("max"), fourthParameter.get());
                                    break;
                                }
                            }

                            if (fourthParameter.isEmpty() && (command[3].equals("min") || command[3].equals("max"))) {
                                // weird IDE bug, had to move the if down here ¯\_(ツ)_/¯
                                if (thirdParameter.isPresent()) {
                                    de = new ModifiedRollXExtremumEvent(event, x, command[3].equals("max"), thirdParameter.get());
                                    break;
                                }
                            }

                            if (thirdParameter.isEmpty() && fourthParameter.isEmpty()) {
                                sendMessage(event, "At least one argument has to be a number sir.");
                                return true;
                            }
                        }
                    }
                    break;

                case "!rolla":
                    if (command.length == 2) {
                        de = new AttributeRoleEvent(event, command[1]);
                    } else if (command.length == 3) {
                        Optional<Integer> modifier = parseInt(command[2], event, "The modifier must be a NUMBER you know?");

                        if (modifier.isEmpty())
                            break;

                        de = new ModifiedAttributeRoleEvent(event, command[1], modifier.get());
                    }
                    break;

                case "!prollx":
                    if (command.length >= 2) {
                        Optional<Integer> numberOfRollsOptional = parseInt(
                                command[1], event,
                                String.format("Please give me a detailed description on how i'm able to roll %s dice", command[1]));

                        if (numberOfRollsOptional.isEmpty())
                            return true;

                        int x = numberOfRollsOptional.get();

                        if (command.length == 2) {
                            de = new PartyRollXEvent(event, x);
                            break;
                        }

                        if (command.length == 3) {
                            Optional<Integer> modifierOptional = parseInt(
                                    command[2], event,
                                    String.format("Please give me a detailed description on how to add %s do a roll...", command[2])
                            );

                            if (modifierOptional.isEmpty())
                                return true;

                            de = new ModifiedPartyRollXEvent(event, x, modifierOptional.get());
                        }
                    }
                    break;

                case "!umod":
                    if (command.length == 2) {
                        try {
                            int x = Integer.parseInt(command[1]);
                            de = new DiceModifierEvent(event, x);
                        } catch (NumberFormatException e) {
                            sendMessage(event, String.format("Please give me a detailed explanation about how many faces '%s' are...", command[1]));
                        }
                    } else if (command.length > 2) {
                        sendMessage(event, "If you want two modifiers, just add them together. I'm not your butler or something");
                        return true;
                    }
                    break;

                case "!udice":
                    if (command.length == 2) {
                        try {
                            removeMessage(event);
                            int x = Integer.parseInt(command[1]);
                            de = new DiceTypeEvent(event, x);
                        } catch (NumberFormatException e) {
                            sendMessage(event, String.format("Roll your '%s'-sided dice yourself, sir.", command[1]));
                        }
                    } else if (command.length == 1) {
                        sendMessage(event, "If you don't want to throw a die, just don't!");
                        return true;
                    }
                    break;

                case "!rollo":
                    //Syntax: !rollt [target] [nr] [tame] [nr] ...
                    //so command.length has to be uneven and not one
                    if ((command.length % 2) == 1 && command.length != 1) {
                        try {
                            List<Target> targets = new LinkedList<>();

                            for (int i = 1; i < command.length; i += 2)
                                targets.add(new Target(command[i], Integer.parseInt(command[i + 1])));

                            de = new OtherRollEvent(event, targets);
                        } catch (NumberFormatException e) {
                            sendMessage(event, "Try to use actual numbers for the roll-amounts please.\nRemember, the syntax is: !rollt [target] [nr] [tame] [nr] ...");
                        }
                    } else if (command.length == 1) {
                        sendMessage(event, "I think you forgot to tell me something.");
                        return true;
                    } else {
                        sendMessage(event, "I need a name and a number corresponding to it.");
                        return true;
                    }
                    break;

                case "!rollt":
                    //Syntax: !rollt [target] [nr] [tame] [nr] ...
                    //so command.length has to be uneven and not one
                    if ((command.length % 2) == 1 && command.length != 1) {
                        try {
                            List<Target> targets = new LinkedList<>();

                            for (int i = 1; i < command.length; i += 2)
                                targets.add(new Target(command[i], Integer.parseInt(command[i + 1])));

                            de = new TargetedRollEvent(event, targets);
                        } catch (NumberFormatException e) {
                            sendMessage(event, "Try to use actual numbers for the roll-amounts please.\nRemember, the syntax is: !rollt [target] [nr] [tame] [nr] ...");
                        }
                    } else if (command.length == 1) {
                        sendMessage(event, "I think you forgot to tell me something.");
                        return true;
                    } else {
                        sendMessage(event, "I need a name and a number corresponding to it.");
                        return true;
                    }
                    break;

                case "!rollc":
                    if (command.length >= 2) {
                        Optional<Integer> numberOfRollsOptional = parseInt(command[1], event, String.format("Please give me a detailed explanation about how I am able to roll a dice '%s' times.", command[1]));

                        if (numberOfRollsOptional.isEmpty())
                            return true;

                        if (command.length == 2) {
                            de = new CumulativeRollEvent(event, numberOfRollsOptional.get());
                            break;
                        }

                        if (command.length == 3) {
                            Optional<Integer> optionalModifier = parseInt(command[2], event, String.format("Please give me a detailed explanation about how I am able to add '%s' to a dicethrow...", command[2]));

                            if (optionalModifier.isEmpty())
                                return true;

                            de = new ModifiedCumulativeRollEvent(event, numberOfRollsOptional.get(), optionalModifier.get());
                        }
                    }
                    break;

                case "!link":
                    if (command.length == 2)
                        de = new LinkEvent(event, command[1]);
                    break;
                default:
                    return false;
            }

            if (de != null)
                de.handle();
        }

        return false;
    }
}
