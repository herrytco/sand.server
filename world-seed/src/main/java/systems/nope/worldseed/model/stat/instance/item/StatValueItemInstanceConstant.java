package systems.nope.worldseed.model.stat.instance.item;

import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.model.stat.value.StatValueConstant;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class StatValueItemInstanceConstant extends StatValueItemInstance {
    @NotNull
    private Integer value;

    public StatValueItemInstanceConstant() {
        setType("constant");
    }

    public StatValueItemInstanceConstant(World world, StatValue statValue, Item item, @NotNull Integer value) {
        super(world, statValue, item, "constant");
        setType("constant");
        this.value = value;
    }

    public static StatValueItemInstanceConstant fromStatValueAndItem(StatValueConstant statValue, Item item) {
        return new StatValueItemInstanceConstant(
                statValue.getWorld(),
                statValue,
                item,
                statValue.getInitalValue()
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
