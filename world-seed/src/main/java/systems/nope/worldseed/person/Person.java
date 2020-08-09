package systems.nope.worldseed.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.stat.StatSheet;
import systems.nope.worldseed.stat.StatValueInstance;
import systems.nope.worldseed.world.World;

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
            inverseJoinColumns = @JoinColumn(name = "stat_sheet_id")
    )
    private List<StatSheet> statSheets;

    @OneToMany(mappedBy = "id")
    private List<StatValueInstance> statValues;

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
}
