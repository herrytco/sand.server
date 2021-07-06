package systems.nope.worldseed.dto.stat.item;

import systems.nope.worldseed.dto.stat.StatValueConstantDto;
import systems.nope.worldseed.dto.stat.StatValueDto;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;
import systems.nope.worldseed.model.stat.value.StatValueConstant;

public class StatValueItemInstanceDto {
    private final int id;

    private final StatValueDto statValue;

    private final String type;

    private final int value;

    public StatValueItemInstanceDto(Integer id, StatValueDto statValue, String type, Integer value) {
        this.id = id;
        this.statValue = statValue;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public StatValueDto getStatValue() {
        return statValue;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public static StatValueItemInstanceDto fromInstance(StatValueInstance instance) {
        return new StatValueItemInstanceDto(
                instance.getId(),
                StatValueConstantDto.fromStatValueConstant((StatValueConstant) instance.getStatValue()),
                instance.getType(),
                instance.getValue()
        );
    }
}
