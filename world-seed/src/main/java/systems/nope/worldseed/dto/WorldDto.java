package systems.nope.worldseed.dto;

import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.StatSheet;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldDto {
    private final int id;

    private final String name;

    private final String description;

    private final List<Integer> persons;

    private final List<Integer> sheets;

    private final String seed;

    private WorldDto(int id, String name, String description, List<Integer> persons, List<Integer> sheets, String seed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.persons = persons;
        this.sheets = sheets;
        this.seed = seed;
    }

    public static WorldDto fromWorld(World world) {
        return new WorldDto(
                world.getId(),
                world.getName(),
                world.getDescription(),
                world.getPersons() == null
                        ? new LinkedList<>()
                        : world.getPersons().stream().map(Person::getId).collect(Collectors.toList()),
                world.getSheets() == null
                        ? new LinkedList<>()
                        : world.getSheets().stream().map(StatSheet::getId).collect(Collectors.toList()),
                world.getSeed()
        );
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Integer> getPersons() {
        return persons;
    }

    public List<Integer> getSheets() {
        return sheets;
    }

    public String getSeed() {
        return seed;
    }
}
