package systems.nope.worldseed.dto.stat.item;

import systems.nope.worldseed.dto.stat.StatValueConstantDto;
import systems.nope.worldseed.dto.stat.StatValueDto;
import systems.nope.worldseed.model.stat.instance.item.StatValueItemInstance;
import systems.nope.worldseed.model.stat.value.StatValueConstant;

public class StatValueItemInstanceDto {
    private final int id;

    private final StatValueDto statValue;

    private final int item;

    private final String type;

    private final int value;

    public StatValueItemInstanceDto(Integer id, StatValueDto statValue, Integer item, String type, Integer value) {
        this.id = id;
        this.statValue = statValue;
        this.item = item;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public StatValueDto getStatValue() {
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
                StatValueConstantDto.fromStatValueConstant((StatValueConstant) instance.getStatValue()),
                instance.getItem().getId(),
                instance.getType(),
                instance.getValue()
        );
    }
}
