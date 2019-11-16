package systems.nope.sand.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Session {
    @Id
    @GeneratedValue
    private int id;

    public Session() {
    }

    public Session(@NotBlank String name, String description) {
        this.name = name;
        this.description = description;
    }

    @NotBlank
    private String name;

    private String description;

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
}
