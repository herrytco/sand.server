package systems.nope.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import systems.nope.discord.eventlistener.dice.DiceHandler;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class DiscordApplication {

    public static void main(String[] args) {
	System.out.println("Starting up the bot my nigga!");
        try {
            JDA jda = new JDABuilder(DiscordConstants.token)
                    .addEventListeners(new DiceHandler())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
