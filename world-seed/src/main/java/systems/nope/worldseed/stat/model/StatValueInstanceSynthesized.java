package systems.nope.worldseed.stat.model;

import systems.nope.worldseed.person.Person;
import systems.nope.worldseed.world.World;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class StatValueInstanceSynthesized extends StatValueInstance {
    @Transient
    private Integer value;

    public StatValueInstanceSynthesized() {
    }

    public StatValueInstanceSynthesized(World world, StatValue statValue, Person person) {
        super(world, statValue, person);
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
