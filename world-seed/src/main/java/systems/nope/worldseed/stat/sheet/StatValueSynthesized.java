package systems.nope.worldseed.stat.model;

import systems.nope.worldseed.world.World;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class StatValueSynthesized extends StatValue{
    @NotBlank
    private String formula;

    public StatValueSynthesized() {
    }

    public StatValueSynthesized(StatSheet sheet, String name, String nameShort, String unit, World world, @NotBlank String formula) {
        super(sheet, name, nameShort, unit, world);
        this.formula = formula;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
