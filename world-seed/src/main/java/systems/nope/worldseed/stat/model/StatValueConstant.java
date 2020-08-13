package systems.nope.worldseed.stat.model;

import systems.nope.worldseed.world.World;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stat_value_constant")
public class StatValueConstant extends StatValue {
    @Column(name = "initial")
    private Integer initalValue;

    public StatValueConstant() {
    }

    public StatValueConstant(StatSheet sheet, String name, String nameShort, String unit, World world, Integer initalValue) {
        super(sheet, name, nameShort, unit, world);
        this.initalValue = initalValue;
    }

    public Integer getInitalValue() {
        return initalValue;
    }

    public void setInitalValue(Integer initalValue) {
        this.initalValue = initalValue;
    }
}
