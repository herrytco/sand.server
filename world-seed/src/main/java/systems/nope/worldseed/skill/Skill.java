package systems.nope.worldseed.skill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import systems.nope.worldseed.document.Document;
import systems.nope.worldseed.world.World;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Skill {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String name;

//    @OneToOne
//    @JoinColumn(name = "document")
//    private Document document;

    @ManyToOne
    @JoinColumn(name = "world")
    @JsonIgnore
    private World world;

    public Skill(String name, Document document, World world) {
        this.name = name;
//        this.document = document;
        this.world = world;
    }

    public Skill() {
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

//    public Document getDocument() {
//        return document;
//    }
//
//    public void setDocument(Document document) {
//        this.document = document;
//    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
