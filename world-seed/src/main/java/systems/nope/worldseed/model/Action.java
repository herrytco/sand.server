package systems.nope.worldseed.model;

import systems.nope.worldseed.model.stat.StatSheet;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Action {
    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String description;

    @NotBlank
    @NotNull
    private String formula;

    @NotBlank
    @NotNull
    private String invokeMessage;

    @ManyToMany
    @JoinTable(
            name = "action_required_stat_sheet",
            joinColumns = @JoinColumn(name = "action_id"),
            inverseJoinColumns = @JoinColumn(name = "stat_sheet_id")
    )
    private Set<StatSheet> requiredStatSheets;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Set<StatSheet> getRequiredStatSheets() {
        return requiredStatSheets;
    }

    public void setRequiredStatSheets(Set<StatSheet> requiredStatSheets) {
        this.requiredStatSheets = requiredStatSheets;
    }

    public String getInvokeMessage() {
        return invokeMessage;
    }

    public void setInvokeMessage(String invokeMessage) {
        this.invokeMessage = invokeMessage;
    }
}
