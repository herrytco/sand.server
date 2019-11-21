package systems.nope.sand.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
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

    @Column(name = "link_world_anvil")
    private String worldAnvilLink;

    @OneToMany(mappedBy = "world")
    @JsonIgnore
    private List<WorldAssignment> users;

    public World() {
    }

    public World(@NotBlank String name, String description, String worldAnvilLink) {
        this.name = name;
        this.description = description;
        this.worldAnvilLink = worldAnvilLink;
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

    public String getWorldAnvilLink() {
        return worldAnvilLink;
    }

    public void setWorldAnvilLink(String worldAnvilLink) {
        this.worldAnvilLink = worldAnvilLink;
    }
}
