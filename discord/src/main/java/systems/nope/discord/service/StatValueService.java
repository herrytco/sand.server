package systems.nope.discord.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import systems.nope.discord.constants.ServerConstants;
import systems.nope.discord.model.person.StatSheet;
import systems.nope.discord.model.person.StatValue;
import systems.nope.discord.util.BackendUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatValueService {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<StatValue> getValuesForStatSheet(StatSheet sheet) throws IOException {
        List<StatValue> valueList = new LinkedList<>();

        String values = BackendUtil.sendRequest(String.format("%s/stat-values/stat-sheet/id/%d", ServerConstants.urlBackend(), sheet.getId()));

        try {
            List<Map<String, Object>> data = mapper.readValue(values, List.class);

            for (Map<String, Object> row : data)
                valueList.add(StatValue.fromJson(row));

            return valueList;
        } catch (MismatchedInputException | JsonParseException e) {
            System.out.println("Unable to decode Stat-Sheet response: " + values);
        }

        return new LinkedList<>();
    }


}
