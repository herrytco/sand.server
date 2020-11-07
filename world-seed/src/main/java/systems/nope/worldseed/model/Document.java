package systems.nope.worldseed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "document")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Document {

    /**
     * marks the unique id for that DB row (changes during updates)
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * marks the id of the document (stays the same during updates)
     */
    @NotNull
    private Integer i;

    /**
     * marks the version of the document higher->later
     */
    @NotNull
    private Integer version;


    @NotBlank
    private String richtext;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    public Document() {
    }

    public Document(@NotNull Integer i, @NotNull Integer version, @NotBlank String richtext, World world) {
        this.i = i;
        this.version = version;
        this.richtext = richtext;
        this.world = world;
    }

    public Document(@NotNull Integer i, @NotBlank String richtext, World world) {
        this(i, 1, richtext, world);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
