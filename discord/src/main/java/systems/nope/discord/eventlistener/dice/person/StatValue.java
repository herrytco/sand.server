package systems.nope.discord.eventlistener.dice.person;

import java.util.Map;

public class StatValue {
    private final int id;

    private final String name;

    private final String nameShort;

    private final String unit;

    private final String type;

    private Integer value;

    public StatValue(int id, String name, String nameShort, String unit, String type) {
        this.id = id;
        this.name = name;
        this.nameShort = nameShort;
        this.unit = unit;
        this.type = type;
    }

    public static StatValue fromJson(Map<String, Object> json) {
        return new StatValue(
                (Integer) json.get("id"),
                (String) json.get("name"),
                (String) json.get("nameShort"),
                (String) json.get("unit"),
                (String) json.get("type")
        );
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public String getUnit() {
        return unit;
    }

    public String getType() {
        return type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
