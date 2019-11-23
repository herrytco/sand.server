package systems.nope.discordintegration;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import systems.nope.discordintegration.eventhandlers.DiceMessageHandler;

import javax.security.auth.login.LoginException;

public class Application {
    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(DiscordConstants.discordToken)
                    .addEventListeners(new DiceMessageHandler())
                    .build();
        } catch (LoginException e) {
            System.out.println("Login not possible.");
        }
    }
}
