package systems.nope.worldseed.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Tileset {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "world_id")
    private World world;

    @NotBlank
    private String name;

    @NotNull
    private Integer tileWidth;

    @NotNull
    private Integer tileHeight;

    public Tileset() {
    }

    public Tileset(World world, String name, Integer tileWidth, Integer tileHeight) {
        this.world = world;
        this.name = name;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
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

    public Integer getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(Integer tileWidth) {
        this.tileWidth = tileWidth;
    }

    public Integer getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(Integer tileHeight) {
        this.tileHeight = tileHeight;
    }
}
