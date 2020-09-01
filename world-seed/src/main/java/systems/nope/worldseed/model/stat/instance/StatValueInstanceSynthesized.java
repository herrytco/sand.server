package systems.nope.worldseed.model.stat.instance;

import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;

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

    public static StatValueInstanceSynthesized fromStatValueAndPerson(StatValueSynthesized valueNew, Person assignedPerson) {
        return new StatValueInstanceSynthesized(
                valueNew.getWorld(),
                valueNew,
                assignedPerson
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
