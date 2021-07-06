package systems.nope.discord.util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import systems.nope.discord.event.DiceEvent;
import systems.nope.discord.event.person.ActionEvent;
import systems.nope.discord.event.person.AttributeRoleEvent;
import systems.nope.discord.event.HelpEvent;
import systems.nope.discord.event.modifiers.DiceTypeEvent;
import systems.nope.discord.event.modifiers.UmodEvent;
import systems.nope.discord.event.party.CreatePartyEvent;
import systems.nope.discord.event.party.PartyRollEvent;
import systems.nope.discord.event.party.PartyRollXEvent;
import systems.nope.discord.event.person.LinkEvent;
import systems.nope.discord.event.person.UnlinkEvent;
import systems.nope.discord.event.rolls.*;
import systems.nope.discord.event.statistics.ServerAverageEvent;
import systems.nope.discord.event.statistics.ServerAverageResetEvent;
import systems.nope.discord.event.statistics.UserAverageEvent;
import systems.nope.discord.event.statistics.UserAverageResetEvent;
import systems.nope.discord.exceptions.ParseException;

public class CommandUtil {
    public static DiceEvent parseCommand(String command, MessageReceivedEvent event) throws ParseException {
        String[] parts = command.split(" ");

        switch (parts[0]) {
            case "!action":
                return ActionEvent.fromString(event, parts);

            case "!link":
                return LinkEvent.fromString(event, parts);

            case "!unlink":
                return new UnlinkEvent(event);

            case "!rolla":
                return AttributeRoleEvent.fromString(event, parts);

            case "!party":
                return new CreatePartyEvent(event);

            case "!proll":
                return PartyRollEvent.fromString(event, parts);

            case "!prollx":
                return PartyRollXEvent.fromString(event, parts);

            case "!roll":
                return RollEvent.fromString(event, parts);

            case "!rollx":
                return RollXEvent.fromString(event, parts);

            case "!froll":
            case "!rollf":
                return FreeRollEvent.fromString(event, parts);

            case "!rollc":
                return RollCEvent.fromString(event, parts);

            case "!rolle":
                return ExtremeRollEvent.fromString(event, parts);

            case "!umod":
                return UmodEvent.fromString(event, parts);

            case "!udice":
                return DiceTypeEvent.fromString(event, parts);

            case "!dicehelp":
                return new HelpEvent(event);

            case "!uavg":
                return new UserAverageEvent(event);

            case "!uavgreset":
                return new UserAverageResetEvent(event);

            case "!savg":
                return new ServerAverageEvent(event);

            case "!savgreset":
                return new ServerAverageResetEvent(event);

            default:
                return null;
        }
    }

}
