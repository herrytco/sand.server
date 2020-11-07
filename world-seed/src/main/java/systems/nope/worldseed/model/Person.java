package systems.nope.worldseed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;

import javax.persistence.*;
import java.util.List;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    private String name;

    private String apiKey;

    @OneToMany
    @JoinTable(
            name = "person_stat_sheet",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "stat_sheet_id_2")
    )
    private List<StatSheet> statSheets;

    @OneToMany(mappedBy = "person")
    private List<StatValueInstance> statValues;

    @ManyToMany
    @JoinTable(
            name = "person_item",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;

    public Person(World world, String name, String apiKey) {
        this.world = world;
        this.name = name;
        this.apiKey = apiKey;
    }

    public Person() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<StatSheet> getStatSheets() {
        return statSheets;
    }

    public void setStatSheets(List<StatSheet> statSheets) {
        this.statSheets = statSheets;
    }

    public List<StatValueInstance> getStatValues() {
        return statValues;
    }

    public void setStatValues(List<StatValueInstance> statValues) {
        this.statValues = statValues;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
