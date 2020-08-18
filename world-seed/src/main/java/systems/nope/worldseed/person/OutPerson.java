package systems.nope.worldseed.person;

import systems.nope.worldseed.stat.model.StatValueInstance;
import systems.nope.worldseed.stat.sheet.StatSheet;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class OutPerson {
    private final int id;

    private final int world;

    private final String name;

    private final String apiKey;

    private final List<Integer> sheets;

    private final List<Integer> stats;

    private OutPerson(int id, int world, String name, String apiKey, List<Integer> sheets, List<Integer> stats) {
        this.id = id;
        this.world = world;
        this.name = name;
        this.apiKey = apiKey;
        this.sheets = sheets;
        this.stats = stats;
    }

    public static OutPerson fromPerson(Person person) {
        return new OutPerson(
                person.getId(),
                person.getWorld().getId(),
                person.getName(),
                person.getApiKey(),
                person.getStatSheets() != null ? person.getStatSheets().stream().map(StatSheet::getId).collect(Collectors.toList()) : new LinkedList<>(),
                person.getStatValues() != null ? person.getStatValues().stream().map(StatValueInstance::getId).collect(Collectors.toList()) : new LinkedList<>()
        );
    }

    public int getId() {
        return id;
    }

    public int getWorld() {
        return world;
    }

    public String getName() {
        return name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public List<Integer> getSheets() {
        return sheets;
    }

    public List<Integer> getStats() {
        return stats;
    }
}
