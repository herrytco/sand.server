package systems.nope.worldseed.model.person;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class PersonAccess {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String name;

    public PersonAccess() {}

    public PersonAccess(int id, String name) {
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
}
