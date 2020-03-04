package systems.nope.worldseed.stat;

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

    @OneToMany
    @JoinColumn(name = "id")
    List<StatValue> statValues;

    public StatSheet() {
    }

    public StatSheet(int id, String name) {
        this.id = id;
        this.name = name;
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

    public List<StatValue> getStatValues() {
        return statValues;
    }

    public void setStatValues(List<StatValue> statValues) {
        this.statValues = statValues;
    }
}
