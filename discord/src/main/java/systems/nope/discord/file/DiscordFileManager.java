package systems.nope.discord.file;

public class DiscordFileManager extends FileManager {
    public static final String keyDiscordToken = "discord-key";
    public static final String keyJwtToken = "jwt-token";

    public DiscordFileManager() {
        super("discord-api-key");
    }
}
