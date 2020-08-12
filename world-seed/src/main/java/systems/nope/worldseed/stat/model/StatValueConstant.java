package systems.nope.worldseed.stat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stat_value_constant")
public class StatValueConstant extends StatValue {
    @Column(name = "initial")
    private Integer initalValue;

    public Integer getInitalValue() {
        return initalValue;
    }

    public void setInitalValue(Integer initalValue) {
        this.initalValue = initalValue;
    }
}
