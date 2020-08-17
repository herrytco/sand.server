package systems.nope.worldseed.stat.model;

import systems.nope.worldseed.person.Person;
import systems.nope.worldseed.stat.sheet.StatValue;
import systems.nope.worldseed.world.World;

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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
