package systems.nope.discord.eventlistener.dice.person;

import java.util.Map;

public class Stat {
    private final String name;
    private final String nameShort;
    private final Integer value;

    public static Stat fromMap(Map<String, Object> data) {
        if(!data.containsKey("name") || !data.containsKey("nameShort") || !data.containsKey("value"))
            throw new IllegalArgumentException("Missing fields!");

        return new Stat(
                (String) data.get("name"),
                (String) data.get("nameShort"),
                (Integer) data.get("value")
        );
    }

    public Stat(String name, String nameShort, Integer value) {
        this.name = name;
        this.nameShort = nameShort;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public Integer getValue() {
        return value;
    }
}
