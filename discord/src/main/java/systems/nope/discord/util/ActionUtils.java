package systems.nope.discord.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import systems.nope.discord.constants.EmoteConstants;
import systems.nope.discord.constants.ServerConstants;
import systems.nope.discord.model.person.Person;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ActionUtils {
    public static String invokeAction(int itemId, int actionId, Person person) throws IOException {
        String actions = BackendUtil.sendRequest(
                String.format(
                        "%s/actions/%d/items/%d/persons/%d",
                        ServerConstants.urlBackend(),
                        actionId,
                        itemId,
                        person.getId()
                )
        );

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(actions, HashMap.class);

        String message = (String) data.get("invokeMessage");

        return message.replaceAll("\\$ICON", EmoteConstants.emoteAttack);
    }
}
