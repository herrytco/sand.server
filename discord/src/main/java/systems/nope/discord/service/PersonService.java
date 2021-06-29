package systems.nope.discord.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import systems.nope.discord.constants.ServerConstants;
import systems.nope.discord.exceptions.ServerException;
import systems.nope.discord.model.person.Person;
import systems.nope.discord.model.person.StatSheet;
import systems.nope.discord.model.person.StatValue;
import systems.nope.discord.util.BackendUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Person getPersonForApiKey(String apiKey) throws IOException, ServerException {
        String token = BackendUtil.getToken();

        if (token == null)
            throw new ServerException("No token available!");

        String response = BackendUtil.sendRequest(String.format("%s/persons/api/%s", ServerConstants.urlBackend(), apiKey));

        try {
            HashMap<String, Object> data = mapper.readValue(response, HashMap.class);
            Person linkedPerson = Person.fromJson(data);
            enhancePerson(linkedPerson);

            return linkedPerson;
        } catch (MismatchedInputException | JsonParseException e) {
            if (apiKey != null)
                System.out.println("No data for apiKey: " + apiKey.substring(0, Math.min(apiKey.length(), 20)) + "...");
            else
                System.out.println("No API key stored.");
            return null;
        }
    }

    /**
     * query statsheet and -value information from the backend and store it
     * in the Person given
     *
     * @param person - target person
     * @throws IOException - problem during HTTP communication
     */
    private static void enhancePerson(Person person) throws IOException {
        List<StatSheet> sheetList = StatSheetService.getStatSheetsForPerson(person);
        person.setStatSheets(sheetList);

        // fetch stat values for each sheet
        for (StatSheet sheet : person.getStatSheets()) {
            List<StatValue> values = StatValueService.getValuesForStatSheet(sheet);
            sheet.setValues(values);
        }

        // fetch stat value INSTANCES for each sheet
        String statValueInstances = BackendUtil.sendRequest(String.format("%s/stat-value-instances/person/id/%d", ServerConstants.urlBackend(), person.getId()));
        try {
            List<Map<String, Object>> data = mapper.readValue(statValueInstances, List.class);

            for (Map<String, Object> row : data) {
                Integer statValueId = (Integer) row.get("statValue");

                for (StatSheet sheet : person.getStatSheets()) {

                    boolean found = false;

                    for (StatValue value : sheet.getValues()) {
                        if (value.getId() == statValueId) {
                            value.setValue((Integer) row.get("value"));
                            found = true;
                            break;
                        }
                    }

                    if (found)
                        break;
                }
            }
        } catch (MismatchedInputException | JsonParseException e) {
            System.out.println("Unable to decode Stat-Sheet response: " + statValueInstances);
        }
    }


}
