package systems.nope.worldseed.util.file;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.tile.Tile;

import java.io.File;
import java.io.IOException;

@Service
public class TileFileUtil extends FileUtil {
    public TileFileUtil() {
        super("tiles");
    }

    public File getTileFilehandler(Tile tile) throws IOException {
        return resolve(
                tile.getTileset().getWorld(),
                String.format("%d/%d.png", tile.getTileset().getId(), tile.getId())
        );
    }
}
