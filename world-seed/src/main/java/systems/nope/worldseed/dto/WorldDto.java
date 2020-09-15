package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.tile.Tileset;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class WorldDto {
    private final Integer id;

    private final String name;

    private final String description;

    private final List<Integer> persons;

    private final List<Integer> sheets;

    private final List<Integer> tilesets;

    private final String seed;

    private WorldDto(Integer id, String name, String description, List<Integer> persons, List<Integer> sheets, List<Integer> tilesets, String seed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.persons = persons;
        this.sheets = sheets;
        this.seed = seed;
        this.tilesets = tilesets;
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
                world.getTilesets() == null
                        ? new LinkedList<>()
                        : world.getTilesets().stream().map(Tileset::getId).collect(Collectors.toList()),
                world.getSeed()
        );
    }
}
