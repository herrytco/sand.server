package systems.nope.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import systems.nope.discord.eventlistener.DiceHandler;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class DiscordApplication {

    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(DiscordConstants.token)
                    .addEventListeners(new DiceHandler())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
