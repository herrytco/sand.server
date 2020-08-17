package systems.nope.worldseed.stat.sheet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.world.World;

import javax.persistence.*;

@Entity
@Table(name = "stat_value")
@Inheritance(strategy = InheritanceType.JOINED)
public class StatValue {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "stat_sheet_id")
    @JsonIgnore
    private StatSheet sheet;

    private String name;

    private String nameShort;

    private String unit;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    public StatValue() {
    }

    public StatValue(StatSheet sheet, String name, String nameShort, String unit, World world) {
        this.sheet = sheet;
        this.name = name;
        this.nameShort = nameShort;
        this.unit = unit;
        this.world = world;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatSheet getSheet() {
        return sheet;
    }

    public void setSheet(StatSheet sheet) {
        this.sheet = sheet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
