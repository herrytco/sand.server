package systems.nope.discord.eventlistener.dice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import systems.nope.discord.eventlistener.dice.file.SecretFileManager;

import java.io.IOException;

public class PrivateHandler extends ListenerAdapter {

    private SecretFileManager fileManager = new SecretFileManager();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        message = message.replaceAll("\\s+", " ");

        String[] args = message.split(" ");

        switch (args[0]) {
            case "!secret":
                System.out.println("?> " + message);

                if (event.getChannelType() == ChannelType.PRIVATE) {
                    StringBuilder secretMessage = new StringBuilder();

                    for (int i = 1; i < args.length; i++) {
                        if (i > 1)
                            secretMessage.append(" ");

                        secretMessage.append(args[i]);
                    }

                    Secret secret = new Secret(secretMessage.toString());
                    String json;

                    try {
                        json = new ObjectMapper().writeValueAsString(secret);
                    } catch (JsonProcessingException e) {
                        System.out.println("Not possible to serialize secret!");
                        return;
                    }

                    try {
                        fileManager.putKeyValuePair(secret.getKey(), json);
                    } catch (IOException e) {
                        System.out.println("File persistence did not work for the secret!");
                    }

                    String reply = String.format("I got your secret, you can find it under the key '%s'", secret.getKey());

                    DiscordUtil.sendMessage(event, reply);
                } else if (event.getChannelType() == ChannelType.TEXT) {
                    if (args.length != 2) {
                        DiscordUtil.sendMessage(event, "You have to provide me with a key the secret can be found under. See it as a proof that the secret belongs to you!");
                        return;
                    }

                    String key = args[1];

                    try {
                        String json = (String) fileManager.getValue(key);

                        if(json == null) {
                            DiscordUtil.sendMessage(event, "There is no secret for that key, I am sorry.");
                            DiscordUtil.removeMessage(event);
                            return;
                        }

                        Secret secret = new ObjectMapper().readValue(json, Secret.class);

                        DiscordUtil.sendMessage(
                                event,
                                String.format("The secret of %s was entrusted to me at %s and it says: '%s'.",
                                        DiscordUtil.getMemberName(event.getMember()),
                                        secret.getTime(),
                                        secret.getValue()
                                )
                        );

                        fileManager.deleteKey(secret.getKey());
                        DiscordUtil.removeMessage(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                        DiscordUtil.sendMessage(event, "Something went wrong during the retrieval of the secret, i am terribly sorry.");
                    }
                }

                break;
        }
    }
}
