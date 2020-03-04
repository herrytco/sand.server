package systems.nope.worldseed.stat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "stat_value")
public class StatValue {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "stat_sheet_id", insertable = false, updatable = false)
    @JsonIgnore
    private StatSheet sheet;

    private String name;

    private String nameShort;

    private String unit;

    public StatValue() {
    }

    public StatValue(int id, StatSheet sheet, String name, String nameShort, String unit) {
        this.id = id;
        this.sheet = sheet;
        this.name = name;
        this.nameShort = nameShort;
        this.unit = unit;
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
}
