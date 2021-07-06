package systems.nope.worldseed.dto.stat;

import systems.nope.worldseed.model.stat.value.StatValueSynthesized;

public class StatValueSynthesizedDto extends StatValueDto {
    private final String formula;

    public StatValueSynthesizedDto(int id, String name, String nameShort, int statSheet, String unit, int world,
                                   String type, String formula, Boolean isResource) {
        super(id, name, nameShort, statSheet, unit, world, type, isResource);
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
                statValueSynthesized.getFormula(),
                statValueSynthesized.getResource()
        );
    }

    public String getFormula() {
        return formula;
    }
}
