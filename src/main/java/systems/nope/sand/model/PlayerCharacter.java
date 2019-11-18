package systems.nope.sand.model;

import systems.nope.sand.constants.PlayerConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class PlayerCharacter {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "world_id")
    private World world;

    public PlayerCharacter() {
    }

    public PlayerCharacter(World world, @NotBlank String name, String bodyInfo, String bio) {
        this.world = world;
        this.name = name;
        this.bodyInfo = bodyInfo;
        this.bio = bio;
        energyCurr = PlayerConstants.defaultEnergyMax;
        energyMax = PlayerConstants.defaultEnergyMax;
    }

    @NotBlank
    private String name;

    private String bodyInfo;

    @NotNull
    private Integer energyCurr;

    @NotNull
    private Integer energyMax;

    private String bio;

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

    public String getBodyInfo() {
        return bodyInfo;
    }

    public void setBodyInfo(String bodyInfo) {
        this.bodyInfo = bodyInfo;
    }

    public Integer getEnergyCurr() {
        return energyCurr;
    }

    public void setEnergyCurr(Integer energyCurr) {
        this.energyCurr = energyCurr;
    }

    public Integer getEnergyMax() {
        return energyMax;
    }

    public void setEnergyMax(Integer energyMax) {
        this.energyMax = energyMax;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
