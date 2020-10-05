package systems.nope.discord.eventlistener.dice;

public final class ServerConstants {
    public static final String emoteD20Result1 = "<:nat1:433634880774078465>";
    public static final String emoteD20Result20 = "<:nat20:433634880656506890>";
    public static final String emoteD20Result2To19 = "<:d20unsure:433634881063616512>";
    public static final String emoteRainbowDice = "<:d20rng:433634881118142465>";
    public static final String emoteAttributeIcon = "<:narrator_attribute:742380896786579466>";

    public static final String regularRoll = "%s rolled a %s";
    public static final String rollModification = " %s %d = %d";
    public static final String kthRoll = "%s's %s dice: %s";

    public static final String singleResult = "%s(d%d): %d";
    public static final String calculation = "%s(d%d): %d %s %d = %d";

    public static final String help = "!roll {MOD} ... rolls once with an optional modifier\n!uavg ... prints your average (which is hopefully good)\n!rollx [X] {MOD} ... rolls [X] dice with an optional modifier\n!rollx [X] [min|max] {MOD} ... rolls [X] dice and chooses the best/worst with an optional modifier.\n!rollt [target0] [X0] [target1] [X1] etc ... rolls [X] dice against each [target]\n!rollo [target0] [X0] [target1] [X1] etc ... rolls [X] dice for each [target]\n!umod ... gives you your current dice modifier\n!umod [X] ... sets the modifier for a user to [X] which gets added/subtracted to each roll\n!udice [X] ... continues to roll with an [X]-sided dice\n!rollc [X] {MODIFIER} ... rolls [X] dice and sums the result with an optional modifier\n!uavgreset ... resets your average\n!savg ... prints the average of the whole server\n!savgreset ... prints the average of the whole server\n!help ... prints the help, but I guess you knew that already\n\n!link [api-Key] ... links the discord account to your character\n!rolla [ATTRIBUTE] {MODIFIER} ... rolls once for the selected attribute with an optional modifier\n\n!party ... adds all non-bot members of your current voice channel to your party\n!proll ... rolls once for each !party member\n!prollx [X] {MODIFIER} ... rolls [X] dice for each party member with an optional modifier";

    private static String hostBackend = "http://test.nope-api.systems:30005";

    public static void setHostBackend(String host) {
        System.out.printf("Setting backend host to: '%s'%n", host);
        hostBackend = host;
    }

    public static String urlBackend() {
        return hostBackend;
    }


    public static final String narratorUsername = "narrator@nope-api.systems";
    public static final String narratorPassword = "amrWHr7lx09dny6ygb84";


}
