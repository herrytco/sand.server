package systems.nope.worldseed.dto.stat;

public abstract class StatValueDto {
    private final int id;

    private final String name;

    private final String nameShort;

    private final int statSheet;

    private final String unit;

    private final int world;

    private final String type;

    private final Boolean isResource;

    public StatValueDto(int id, String name, String nameShort, int statSheet, String unit, int world, String type, Boolean isResource) {
        this.id = id;
        this.name = name;
        this.nameShort = nameShort;
        this.statSheet = statSheet;
        this.unit = unit;
        this.world = world;
        this.type = type;
        this.isResource = isResource;
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

    public Boolean getResource() {
        return isResource;
    }
}
