package systems.nope.worldseed.stat.sheet;

import java.util.List;
import java.util.stream.Collectors;

public class OutStatSheet {
    private final int id;

    private final String name;

    private final List<Integer> statValues;

    private final int world;

    private final Integer parent;

    private OutStatSheet(int id, String name, List<Integer> statValues, int world, Integer parent) {
        this.id = id;
        this.name = name;
        this.statValues = statValues;
        this.world = world;
        this.parent = parent;
    }

    public static OutStatSheet fromStatSheet(StatSheet sheet) {

        Integer statSheetId = null;
        if(sheet.getParent() != null)
            statSheetId = sheet.getParent().getId();

        return new OutStatSheet(
                sheet.getId(),
                sheet.getName(),
                sheet.getStatValues().stream().map(StatValue::getId).collect(Collectors.toList()),
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
