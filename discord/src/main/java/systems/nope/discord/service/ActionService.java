package systems.nope.discord.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import systems.nope.discord.constants.EmoteConstants;
import systems.nope.discord.constants.ServerConstants;
import systems.nope.discord.exceptions.ServerException;
import systems.nope.discord.model.person.Action;
import systems.nope.discord.model.person.Person;
import systems.nope.discord.util.BackendUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class ActionService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Action getActionForId(Integer id) throws IOException {
        String action = BackendUtil.sendRequest(String.format("%s/actions/%d", ServerConstants.urlBackend(), id));

        try {
            Map<String, Object> data = mapper.readValue(action, Map.class);

            return new Action(
                    (Integer) data.get("id"),
                    (String) data.get("name"),
                    (String) data.get("description"),
                    (String) data.get("formula")
            );
        } catch (MismatchedInputException | JsonParseException e) {
            System.out.println("Unable to decode Stat-Sheet response: " + action);
        }

        return null;
    }

    public static Optional<String> invokeAction(Person person, Action action) throws IOException {
        String response = BackendUtil.sendRequest(String.format("%s/actions/%d/persons/%d", ServerConstants.urlBackend(), action.getId(), person.getId()));
        System.out.println("Response: "+response);

        try {
            Map<String, Object> data = mapper.readValue(response, Map.class);

            String message = (String) data.get("invokeMessage");
            message = message.replaceAll(
                    "\\$ICON", EmoteConstants.emoteAttack
            );

            return Optional.of(message);
        } catch (MismatchedInputException | JsonParseException e) {
            System.out.println("Unable to decode Action-invoke response: " + action);
        }

        return Optional.empty();
    }


}
