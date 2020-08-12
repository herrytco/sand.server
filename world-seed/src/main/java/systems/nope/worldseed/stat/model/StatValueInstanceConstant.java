package systems.nope.worldseed.stat.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class StatValueInstanceConstant extends StatValueInstance {
    @NotNull
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
