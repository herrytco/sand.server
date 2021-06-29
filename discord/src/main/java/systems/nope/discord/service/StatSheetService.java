package systems.nope.discord.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import systems.nope.discord.constants.ServerConstants;
import systems.nope.discord.model.person.Person;
import systems.nope.discord.model.person.StatSheet;
import systems.nope.discord.util.BackendUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatSheetService {
    private static ObjectMapper mapper = new ObjectMapper();

    public static List<StatSheet> getStatSheetsForPerson(Person person) throws IOException {
        // fetch stat sheets
        String sheets = BackendUtil.sendRequest(String.format("%s/stat-sheets/person/id/%d", ServerConstants.urlBackend(), person.getId()));

        List<StatSheet> sheetList = new LinkedList<>();

        try {
            List<Map<String, Object>> data = mapper.readValue(sheets, List.class);

            for (Map<String, Object> row : data)
                sheetList.add(StatSheet.fromJson(row));

        } catch (MismatchedInputException | JsonParseException e) {
            System.out.println("Unable to decode Stat-Sheet response: " + sheets);
        }

        return sheetList;
    }

}
