package systems.nope.discord.model.person;

import java.util.List;
import java.util.Map;

public class StatSheet {
    private final int id;
    private final String name;
    private final List<Integer> valueIds;

    private List<StatValue> values;

    public StatSheet(int id, String name, List<Integer> values) {
        this.id = id;
        this.name = name;
        this.valueIds = values;
    }

    public static StatSheet fromJson(Map<String, Object> json) {
        return new StatSheet(
                (Integer) json.get("id"),
                (String) json.get("name"),
                (List<Integer>) json.get("statValues")
        );
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getValueIds() {
        return valueIds;
    }

    public List<StatValue> getValues() {
        return values;
    }

    public void setValues(List<StatValue> values) {
        this.values = values;
    }
}
