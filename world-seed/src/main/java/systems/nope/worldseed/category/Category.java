package systems.nope.worldseed.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.world.World;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    private String name;

    private String color;

    public Category(World world, String name, String color) {
        this.world = world;
        this.name = name;
        this.color = color;
    }

    public Category() {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
