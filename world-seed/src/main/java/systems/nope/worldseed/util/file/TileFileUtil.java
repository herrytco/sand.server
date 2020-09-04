package systems.nope.worldseed.util.file;

import org.springframework.stereotype.Service;

@Service
public class TileFileUtil extends FileUtil {
    public TileFileUtil() {
        super("tiles");
    }
}
