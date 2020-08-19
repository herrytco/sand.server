package systems.nope.worldseed.dto;

import systems.nope.worldseed.model.stat.value.StatValueConstant;

public class StatValueDto {
    private final int id;

    private final String name;

    private final String nameShort;

    private final int statSheet;

    private final String unit;

    private final int world;

    private final String type;

    public StatValueDto(int id, String name, String nameShort, int statSheet, String unit, int world, String type) {
        this.id = id;
        this.name = name;
        this.nameShort = nameShort;
        this.statSheet = statSheet;
        this.unit = unit;
        this.world = world;
        this.type = type;
    }

    public static StatValueDto fromStatValueConstant(StatValueConstant statValueConstant) {
        return new StatValueDto(
                statValueConstant.getId(),
                statValueConstant.getName(),
                statValueConstant.getNameShort(),
                statValueConstant.getSheet().getId(),
                statValueConstant.getUnit(),
                statValueConstant.getWorld().getId(),
                statValueConstant.getType()
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

    public int getStatSheet() {
        return statSheet;
    }

    public String getUnit() {
        return unit;
    }

    public int getWorld() {
        return world;
    }

    public String getType() {
        return type;
    }
}
