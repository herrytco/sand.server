package systems.nope.worldseed.stat.model;

import systems.nope.worldseed.person.Person;
import systems.nope.worldseed.stat.sheet.StatValue;
import systems.nope.worldseed.world.World;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class StatValueInstanceConstant extends StatValueInstance {
    @NotNull
    private Integer value;

    public StatValueInstanceConstant() {
        setType("constant");
    }

    public StatValueInstanceConstant(World world, StatValue statValue, Person person, @NotNull Integer value) {
        super(world, statValue, person, "constant");
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
