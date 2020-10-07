package systems.nope.worldseed.model.tile;

import javax.persistence.*;

@Entity
public class Tile {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tileset_id")
    private Tileset tileset;

    private String name;

    private String descriptionShort;

    private String descriptionLong;

    private String textColor;

    public Tile(Integer id, Tileset tileset) {
        this.id = id;
        this.tileset = tileset;
    }

    public Tile() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
