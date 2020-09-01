package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;

@Data
public class StatValueSynthesizedDto extends StatValueDto {
    private String formula;

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
}
