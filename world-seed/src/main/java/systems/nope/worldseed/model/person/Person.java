package systems.nope.worldseed.model.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.model.World;
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

    private String portraitImage;

    @OneToMany
    @JoinTable(
            name = "person_stat_sheet",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "stat_sheet_id_2")
    )
    private List<StatSheet> statSheets;

    @ManyToMany
    @JoinTable(
            name = "person_stat_value_instance",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "stat_value_instance_id")}
    )
    private List<StatValueInstance> statValues;

    @ManyToMany
    @JoinTable(
            name = "person_item",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;

    @OneToMany(mappedBy = "person")
    private List<PersonNote> notes;

    @ManyToOne
    @JoinColumn(name = "player")
    private User controllingUser;

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

    public List<PersonNote> getNotes() {
        return notes;
    }

    public void setNotes(List<PersonNote> notes) {
        this.notes = notes;
    }

    public User getControllingUser() {
        return controllingUser;
    }

    public void setControllingUser(User controllingUser) {
        this.controllingUser = controllingUser;
    }

    public String getPortraitImage() {
        return portraitImage;
    }

    public void setPortraitImage(String portraitImage) {
        this.portraitImage = portraitImage;
    }
}
