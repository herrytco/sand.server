package systems.nope.worldseed.stat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.world.World;

import javax.persistence.*;

@Entity
@Table(name = "stat_value_instance")
public class StatValueInstance {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    @ManyToOne
    @JoinColumn(name = "stat_value_id")
    private StatValue statValue;

    private Integer value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public StatValue getStatValue() {
        return statValue;
    }

    public void setStatValue(StatValue statValue) {
        this.statValue = statValue;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
