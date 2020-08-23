package systems.nope.discord.eventlistener.dice.person;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Person {
    private final Integer id;

    private final String name;
    private final List<Stat> stats = new LinkedList<>();

    private final List<Integer> sheetIds;
    private final List<Integer> statValueInstanceIds;

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

    public Integer getId() {
        return id;
    }
}
