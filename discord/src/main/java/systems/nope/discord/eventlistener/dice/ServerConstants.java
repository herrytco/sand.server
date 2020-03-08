package systems.nope.discord.eventlistener.dice;

public final class ServerConstants {
    public static final String emoteD20Result1 = "<:nat1:433634880774078465>";
    public static final String emoteD20Result20 = "<:nat20:433634880656506890>";
    public static final String emoteD20Result2To19 = "<:d20unsure:433634881063616512>";

    public static final String regularRoll = "%s rolled a %s";
    public static final String kthRoll = "%s's %s dice: %s";

    public static final String singleResult = "%s %d";
    public static final String calculation = "%s %d %s %d = %d";

    public static final String help = "!roll ... rolls once\n!uavg ... prints your average (which is hopefully good)\n!rollx [x] ... rolls [x] dice\n!rollt [target0] [x0] [target1] [x1] etc. ... rolls [x] dice for each [target]\n!umod [x] ... sets the modifier for a user to [x] which gets added/subtracted to each roll\n!udice [x] ... continues to roll with an [x]-sided dice\n!rollc [x] ... rolls [x] dice and sums the result\n!uavgreset ... resets your average\n!savg ... prints the average of the whole server\n!savgreset ... prints the average of the whole server\n!help ... prints the help, but I guess you knew that already";
}
