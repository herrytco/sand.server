package systems.nope.worldseed.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.model.Action;
import systems.nope.worldseed.model.Document;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.item.StatValueItemInstance;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Entity
public class Item {
    @Id
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

    @OneToMany(mappedBy = "item")
    private List<StatValueItemInstance> statValueInstances;

    @ManyToOne
    @JoinColumn(name = "description_document")
    private Document descriptionDocument;

    @ManyToMany
    @JoinTable(
            name = "item_action",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
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

    public List<StatValueItemInstance> getStatValueInstances() {
        return statValueInstances;
    }

    public void setStatValueInstances(List<StatValueItemInstance> statValues) {
        this.statValueInstances = statValues;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }
}
