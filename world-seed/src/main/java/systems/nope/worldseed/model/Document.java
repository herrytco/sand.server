package systems.nope.worldseed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Document {

    @Embeddable
    public static class Pk implements Serializable {
        private int id;

        private int version;

        public Pk() {
        }

        public Pk(int id, int version) {
            this.id = id;
            this.version = version;
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

    @ManyToOne
    @JoinColumn(name = "world_id")
    @JsonIgnore
    private World world;

    @NotBlank
    private String title;

    private String content;

    public Pk getId() {
        return id;
    }

    public void setId(Pk id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
