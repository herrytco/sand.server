package systems.nope.discord.model.person;

import java.util.List;
import java.util.Map;

public class Person {
    private final Integer id;

    private final String name;

    private final List<Integer> sheetIds;
    private final List<Integer> statValueInstanceIds;

    private List<StatSheet> statSheets;

    public Person(Integer id, String name, List<Integer> sheetIds, List<Integer> statValueInstanceIds) {
        this.id = id;
        this.name = name;
        this.sheetIds = sheetIds;
        this.statValueInstanceIds = statValueInstanceIds;
    }

    public static Person fromJson(Map<String, Object> json) {
        return new Person(
                (Integer) json.get("id"),
                (String) json.get("name"),
                (List<Integer>) json.get("sheets"),
                (List<Integer>) json.get("stats")
        );
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public List<StatSheet> getStatSheets() {
        return statSheets;
    }

    public void setStatSheets(List<StatSheet> statSheets) {
        this.statSheets = statSheets;
    }

    public List<Integer> getSheetIds() {
        return sheetIds;
    }

    public List<Integer> getStatValueInstanceIds() {
        return statValueInstanceIds;
    }
}
