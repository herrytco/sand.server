package systems.nope.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import systems.nope.discord.handler.DiceHandler;
import systems.nope.discord.handler.PrivateHandler;
import systems.nope.discord.constants.ServerConstants;
import systems.nope.discord.file.DiscordFileManager;

import javax.security.auth.login.LoginException;
import java.io.IOException;

@SpringBootApplication
public class DiscordApplication {
    public static void main(String[] args) throws IOException {

        if (args.length == 1)
            ServerConstants.setHostBackend(args[0]);

        DiscordFileManager fileManager = new DiscordFileManager();

        String key = (String) fileManager.getValue(DiscordFileManager.keyDiscordToken);
        if (key == null) {
            System.out.println("No API key accessible! Add the .json file containing the key at data/discord-api-key/data.json under the key " + DiscordFileManager.keyDiscordToken);
            return;
        }

        try {
            JDA jda = JDABuilder.createDefault(key)
                    .addEventListeners(new DiceHandler())
                    .addEventListeners(new PrivateHandler())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
