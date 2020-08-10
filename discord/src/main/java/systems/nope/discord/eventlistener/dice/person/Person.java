package systems.nope.discord.eventlistener.dice.person;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Person {
    private final String name;
    private final List<Stat> stats = new LinkedList<>();

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStats(List<Map<String, Object>> statList) {
        for(Map<String, Object> stat : statList) {
            Map<String, Object> metaData = (Map<String, Object>) stat.get("statValue");

            stat.put("name", metaData.get("name"));
            stat.put("nameShort", metaData.get("nameShort"));

            stats.add(Stat.fromMap(stat));
        }
    }

    public List<Stat> getStats() {
        return stats;
    }
}
