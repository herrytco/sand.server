package systems.nope.worldseed.model.stat.instance;

import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "stat_value_instance_synthesized")
public class StatValueInstanceSynthesized extends StatValueInstance {
    @Transient
    private Integer value;

    public StatValueInstanceSynthesized() {
        setType("synthesized");
    }

    public StatValueInstanceSynthesized(World world, StatValue statValue) {
        super(world, statValue, "synthesized");
    }

    public static StatValueInstanceSynthesized fromStatValueAndPerson(StatValueSynthesized valueNew) {
        return new StatValueInstanceSynthesized(
                valueNew.getWorld(),
                valueNew
        );
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
