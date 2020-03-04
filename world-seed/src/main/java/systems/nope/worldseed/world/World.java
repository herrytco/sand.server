package systems.nope.worldseed.world;

import systems.nope.worldseed.person.Person;

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

    @OneToMany
    @JoinColumn(name = "id")
    private List<Person> persons;

    public World() {
    }

    public World(String name, String description) {
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

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
