package systems.nope.worldseed.model.stat.instance;

import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.value.StatValue;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class StatValueInstanceSynthesized extends StatValueInstance {
    @Transient
    private Integer value;

    public StatValueInstanceSynthesized() {
        setType("synthesized");
    }

    public StatValueInstanceSynthesized(World world, StatValue statValue, Person person) {
        super(world, statValue, person, "synthesized");
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
