package systems.nope.worldseed.stat.sheet;

public class OutStatValueSynthesized extends OutStatValue {
    private final String formula;

    public OutStatValueSynthesized(int id, String name, String nameShort, int statSheet, String unit, int world, String type, String formula) {
        super(id, name, nameShort, statSheet, unit, world, type);
        this.formula = formula;
    }

    public static OutStatValueSynthesized fromStatValueSynthesized(StatValueSynthesized statValueSynthesized) {
        return new OutStatValueSynthesized(
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
