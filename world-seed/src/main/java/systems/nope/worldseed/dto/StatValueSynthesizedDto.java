package systems.nope.worldseed.dto;

import systems.nope.worldseed.model.stat.value.StatValueSynthesized;

public class StatValueSynthesizedDto extends StatValueDto {
    private final String formula;

    public StatValueSynthesizedDto(int id, String name, String nameShort, int statSheet, String unit, int world, String type, String formula) {
        super(id, name, nameShort, statSheet, unit, world, type);
        this.formula = formula;
    }

    public static StatValueSynthesizedDto fromStatValueSynthesized(StatValueSynthesized statValueSynthesized) {
        return new StatValueSynthesizedDto(
                statValueSynthesized.getId(),
                statValueSynthesized.getName(),
                statValueSynthesized.getNameShort(),
                statValueSynthesized.getSheet().getId(),
                statValueSynthesized.getUnit(),
                statValueSynthesized.getWorld().getId(),
                statValueSynthesized.getType(),
                statValueSynthesized.getFormula()
        );
    }

    public String getFormula() {
        return formula;
    }
}
