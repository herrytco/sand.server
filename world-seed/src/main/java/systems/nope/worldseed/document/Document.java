package systems.nope.worldseed.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import systems.nope.worldseed.world.World;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "document")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Document {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String richtext;

    @NotNull
    Integer version;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    public Document() {
    }

    public Document(String richtext, World world) {
        this(richtext, world, 1);
    }

    public Document(String richtext, World world, int id) {
        this.richtext = richtext;
        this.world = world;
        this.version = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRichtext() {
        return richtext;
    }

    public void setRichtext(String name) {
        this.richtext = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
