package systems.nope.discord.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import systems.nope.discord.constants.ServerConstants;
import systems.nope.discord.model.person.Action;
import systems.nope.discord.util.BackendUtil;

import java.io.IOException;
import java.util.Map;

public class ActionService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Action getActionForId(Integer id) throws IOException {
        String sheets = BackendUtil.sendRequest(String.format("%s/actions/%d", ServerConstants.urlBackend(), id));

        try {
            Map<String, Object> data = mapper.readValue(sheets, Map.class);

            return new Action(
                    (Integer) data.get("id"),
                    (String) data.get("name"),
                    (String) data.get("description"),
                    (String) data.get("formula")
            );
        } catch (MismatchedInputException | JsonParseException e) {
            System.out.println("Unable to decode Stat-Sheet response: " + sheets);
        }

        return null;
    }


}
