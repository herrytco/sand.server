package systems.nope.sand.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class World {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String seed;

    @OneToMany(mappedBy = "world")
    @JsonIgnore
    private List<WorldAssignment> users;

    public World() {
    }

    public World(@NotBlank String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WorldAssignment> getUsers() {
        return users;
    }

    public void setUsers(List<WorldAssignment> users) {
        this.users = users;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }
}
