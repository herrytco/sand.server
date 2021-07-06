package systems.nope.worldseed.model.stat.instance.person;

import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "stat_value_instance_synthesized")
public class StatValuePersonInstanceSynthesized extends StatValuePersonInstance {
    @Transient
    private Integer value;

    public StatValuePersonInstanceSynthesized() {
        setType("synthesized");
    }

    public StatValuePersonInstanceSynthesized(World world, StatValue statValue, Person person) {
        super(world, statValue, person, "synthesized");
    }

    public static StatValuePersonInstanceSynthesized fromStatValueAndPerson(StatValueSynthesized valueNew, Person assignedPerson) {
        return new StatValuePersonInstanceSynthesized(
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
