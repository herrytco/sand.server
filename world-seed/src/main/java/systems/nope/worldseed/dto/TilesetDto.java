package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.model.Tileset;

@Data
public class TilesetDto {
    private Integer id;

    private Integer world;

    private String name;

    private Integer tileWidth;

    private Integer tileHeight;

    public static TilesetDto fromTileset(Tileset tileset) {
        return new TilesetDto(
                tileset.getId(),
                tileset.getWorld().getId(),
                tileset.getName(),
                tileset.getTileWidth(),
                tileset.getTileHeight()
        );
    }

    public TilesetDto(Integer id, Integer world, String name, Integer tileWidth, Integer tileHeight) {
        this.id = id;
        this.world = world;
        this.name = name;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }
}
