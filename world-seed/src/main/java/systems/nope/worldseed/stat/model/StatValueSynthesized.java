package systems.nope.worldseed.stat.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class StatValueSynthesized extends StatValue{
    @NotBlank
    private String formula;

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
