package systems.nope.worldseed.stat.model;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class StatValueInstanceSynthesized extends StatValueInstance {
    @Transient
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
