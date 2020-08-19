package systems.nope.worldseed.model.stat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.value.StatValue;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "stat_sheet")
public class StatSheet {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String name;

    @OneToMany
    @JoinColumn(name = "stat_sheet_id")
    private List<StatValue> statValues;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    @OneToOne
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private StatSheet parent;

    public StatSheet() {
    }

    public StatSheet(String name, World world) {
        this.name = name;
        this.world = world;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public List<StatValue> getStatValues() {
        return statValues;
    }

    public void setStatValues(List<StatValue> statValues) {
        this.statValues = statValues;
    }

    public StatSheet getParent() {
        return parent;
    }

    public void setParent(StatSheet parent) {
        this.parent = parent;
    }
}