package systems.nope.worldseed.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.model.Action;
import systems.nope.worldseed.model.Document;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Entity
public class Item {
    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    @ManyToMany
    @JoinTable(
            name = "item_stat_sheet",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "stat_sheet_id")
    )
    private Set<StatSheet> statSheets;

    @ManyToMany
    @JoinTable(
            name = "item_stat_value_instance",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "stat_value_instance_id")}
    )
    private List<StatValueInstance> statValueInstances;

    @ManyToOne
    @JoinColumn(name = "description_document")
    private Document descriptionDocument;

    @OneToMany(mappedBy = "item")
    private Set<Action> actions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Document getDescriptionDocument() {
        return descriptionDocument;
    }

    public void setDescriptionDocument(Document descriptionDocument) {
        this.descriptionDocument = descriptionDocument;
    }

    public Set<StatSheet> getStatSheets() {
        return statSheets;
    }

    public void setStatSheets(Set<StatSheet> statSheets) {
        this.statSheets = statSheets;
    }

    public List<StatValueInstance> getStatValueInstances() {
        return statValueInstances;
    }

    public void setStatValueInstances(List<StatValueInstance> statValueInstances) {
        this.statValueInstances = statValueInstances;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }
}
