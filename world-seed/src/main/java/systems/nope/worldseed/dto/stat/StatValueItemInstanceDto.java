package systems.nope.worldseed.dto.stat;

import systems.nope.worldseed.model.stat.instance.item.StatValueItemInstance;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstance;

public class StatValueItemInstanceDto {
    private final int id;

    private final int statValue;

    private final int item;

    private final String type;

    private final int value;

    public StatValueItemInstanceDto(Integer id, Integer statValue, Integer item, String type, Integer value) {
        this.id = id;
        this.statValue = statValue;
        this.item = item;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getStatValue() {
        return statValue;
    }

    public int getItem() {
        return item;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public static StatValueItemInstanceDto fromInstance(StatValueItemInstance instance) {
        return new StatValueItemInstanceDto(
                instance.getId(),
                instance.getStatValue().getId(),
                instance.getItem().getId(),
                instance.getType(),
                instance.getValue()
        );
    }
}
