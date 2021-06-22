package systems.nope.worldseed.model.stat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.item.Item;
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

    @OneToMany(mappedBy = "sheet")
    private List<StatValue> statValues;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    @OneToOne
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private StatSheet parent;

    @OneToMany
    @JoinTable(
            name = "person_stat_sheet",
            joinColumns = @JoinColumn(name = "stat_sheet_id_2"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> assignedPersons;

    @OneToMany
    @JoinTable(
            name = "item_stat_sheet",
            joinColumns = @JoinColumn(name = "stat_sheet_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> assignedItems;

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

    public void setAssignedPersons(List<Person> assignPersons) {
        this.assignedPersons = assignPersons;
    }

    public List<Person> getAssignedPersons() {
        return assignedPersons;
    }

    public List<Item> getAssignedItems() {
        return assignedItems;
    }

    public void setAssignedItems(List<Item> assignedItems) {
        this.assignedItems = assignedItems;
    }
}
