package systems.nope.worldseed.dto.stat;

import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.value.StatValue;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class StatSheetDto {
    private final int id;

    private final String name;

    private final List<Integer> statValues;

    private final int world;

    private final Integer parent;

    private StatSheetDto(int id, String name, List<Integer> statValues, int world, Integer parent) {
        this.id = id;
        this.name = name;
        this.statValues = statValues;
        this.world = world;
        this.parent = parent;
    }

    public static StatSheetDto fromStatSheet(StatSheet sheet) {

        Integer statSheetId = null;
        if (sheet.getParent() != null)
            statSheetId = sheet.getParent().getId();

        return new StatSheetDto(
                sheet.getId(),
                sheet.getName(),
                sheet.getStatValues() != null
                        ? sheet.getStatValues().stream().map(StatValue::getId).collect(Collectors.toList())
                        : new LinkedList<>(),
                sheet.getWorld().getId(),
                statSheetId
        );
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getStatValues() {
        return statValues;
    }

    public int getWorld() {
        return world;
    }

    public Integer getParent() {
        return parent;
    }
}
