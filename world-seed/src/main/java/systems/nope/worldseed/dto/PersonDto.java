package systems.nope.worldseed.dto;

import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstance;
import systems.nope.worldseed.model.stat.StatSheet;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonDto {
    private final int id;

    private final int world;

    private final String name;

    private final String apiKey;

    private final List<Integer> sheets;

    private final List<Integer> stats;

    private PersonDto(int id, int world, String name, String apiKey, List<Integer> sheets, List<Integer> stats) {
        this.id = id;
        this.world = world;
        this.name = name;
        this.apiKey = apiKey;
        this.sheets = sheets;
        this.stats = stats;
    }

    public static PersonDto fromPerson(Person person) {
        return new PersonDto(
                person.getId(),
                person.getWorld().getId(),
                person.getName(),
                person.getApiKey(),
                person.getStatSheets() != null ? person.getStatSheets().stream().map(StatSheet::getId).collect(Collectors.toList()) : new LinkedList<>(),
                person.getStatValues() != null ? person.getStatValues().stream().map(StatValuePersonInstance::getId).collect(Collectors.toList()) : new LinkedList<>()
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
