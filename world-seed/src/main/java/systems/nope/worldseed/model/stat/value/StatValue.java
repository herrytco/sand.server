package systems.nope.worldseed.model.stat.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.StatSheet;

import javax.persistence.*;

@Entity
@Table(name = "stat_value")
@Inheritance(strategy = InheritanceType.JOINED)
public class StatValue {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "stat_sheet_id_value")
    @JsonIgnore
    private StatSheet sheet;

    private String name;

    private String nameShort;

    private String unit;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    @Transient
    private String type;

    public StatValue() {
    }

    public StatValue(StatSheet sheet, String name, String nameShort, String unit, World world, String type) {
        this.sheet = sheet;
        this.name = name;
        this.nameShort = nameShort;
        this.unit = unit;
        this.world = world;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
