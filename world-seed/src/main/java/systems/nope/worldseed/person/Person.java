package systems.nope.worldseed.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.world.World;

import javax.persistence.*;

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

    public Person(World world, String name) {
        this.world = world;
        this.name = name;
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
}
