package systems.nope.worldseed.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import systems.nope.worldseed.world.World;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "document")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Document {


    @Embeddable
    public static class Pk implements Serializable {
        int id;

        int version;

        public Pk() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }

    @EmbeddedId
    private Pk id;

    @NotBlank
    private String richtext;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    public Document() {
    }

    public Document(String richtext, World world, int id) {
        this(richtext, world, id, 1);
    }

    public Document(String richtext, World world, int id, int version) {
        this.richtext = richtext;
        this.world = world;
        this.id = new Pk();
        this.id.setId(id);
        this.id.setVersion(version);
    }

    public Pk getId() {
        return id;
    }

    public void setId(Pk id) {
        this.id = id;
    }

    public String getRichtext() {
        return richtext;
    }

    public void setRichtext(String name) {
        this.richtext = name;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
