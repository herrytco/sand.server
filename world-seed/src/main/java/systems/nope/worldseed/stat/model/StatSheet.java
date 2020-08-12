package systems.nope.worldseed.stat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.world.World;

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

    public StatSheet() {
    }

    public StatSheet(int id, String name) {
        this.id = id;
        this.name = name;
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
}
