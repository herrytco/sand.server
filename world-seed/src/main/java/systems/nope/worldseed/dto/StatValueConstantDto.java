package systems.nope.worldseed.dto;

import systems.nope.worldseed.model.stat.value.StatValueConstant;

public class StatValueConstantDto extends StatValueDto {

    private final Integer initialValue;

    public StatValueConstantDto(int id, String name, String nameShort, int statSheet, String unit, int world, String type, Integer initialValue) {
        super(id, name, nameShort, statSheet, unit, world, type);
        this.initialValue = initialValue;
    }

    public static StatValueConstantDto fromStatValueConstant(StatValueConstant statValueConstant) {
        return new StatValueConstantDto(
                statValueConstant.getId(),
                statValueConstant.getName(),
                statValueConstant.getNameShort(),
                statValueConstant.getSheet().getId(),
                statValueConstant.getUnit(),
                statValueConstant.getWorld().getId(),
                statValueConstant.getType(),
                statValueConstant.getInitalValue()
        );
    }

    public Integer getInitialValue() {
        return initialValue;
    }
}
