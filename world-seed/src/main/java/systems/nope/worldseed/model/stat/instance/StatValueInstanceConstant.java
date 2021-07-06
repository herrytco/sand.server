package systems.nope.worldseed.model.stat.instance;

import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.value.StatValue;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "stat_value_instance_constant")
public class StatValueInstanceConstant extends StatValueInstance {
    @NotNull
    private Integer value;

    public StatValueInstanceConstant() {
        setType("constant");
    }

    public StatValueInstanceConstant(World world, StatValue statValue, @NotNull Integer value) {
        super(world, statValue, "constant");
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
