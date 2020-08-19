package systems.nope.worldseed.model.stat.value;

import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.StatSheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stat_value_constant")
public class StatValueConstant extends StatValue {
    @Column(name = "initial")
    private Integer initalValue;

    public StatValueConstant() {
        setType("constant");
    }

    public StatValueConstant(StatSheet sheet, String name, String nameShort, String unit, World world, Integer initalValue) {
        super(sheet, name, nameShort, unit, world, "constant");
        this.initalValue = initalValue;
    }

    public Integer getInitalValue() {
        return initalValue;
    }

    public void setInitalValue(Integer initalValue) {
        this.initalValue = initalValue;
    }
}
