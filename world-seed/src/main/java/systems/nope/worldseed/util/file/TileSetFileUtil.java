package systems.nope.worldseed.util.file;

import org.springframework.stereotype.Service;

@Service
public class TileSetFileUtil extends FileUtil {
    public TileSetFileUtil() {
        super("tilesets");
    }
}
