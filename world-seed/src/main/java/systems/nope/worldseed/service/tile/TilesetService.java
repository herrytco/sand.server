package systems.nope.worldseed.service.tile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.AlreadyExistingException;
import systems.nope.worldseed.exception.FilesystemException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.tile.Tile;
import systems.nope.worldseed.model.tile.Tileset;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.repository.tile.TileRepository;
import systems.nope.worldseed.repository.tile.TilesetRepository;
import systems.nope.worldseed.util.file.TileFileUtil;
import systems.nope.worldseed.util.file.TileSetFileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class TilesetService {

    private final TilesetRepository tilesetRepository;
    private final TileRepository tileRepository;

    private final TileFileUtil tileFileUtil;
    private final TileSetFileUtil tileSetFileUtil;

    private final Logger logger = LoggerFactory.getLogger(TilesetService.class);

    public TilesetService(TilesetRepository tilesetRepository, TileRepository tileRepository, TileFileUtil tileFileUtil, TileSetFileUtil tileSetFileUtil) {
        this.tilesetRepository = tilesetRepository;
        this.tileRepository = tileRepository;
        this.tileFileUtil = tileFileUtil;
        this.tileSetFileUtil = tileSetFileUtil;
    }

    public void updateTile(Integer tilesetId, Integer tileId, String name, String descriptionShort, String descriptionLong, String textColor) {
        Tile tileToUpdate = getTile(get(tilesetId), tileId);

        if (name != null)
            tileToUpdate.setName(name);

        if (descriptionShort != null)
            tileToUpdate.setDescriptionShort(descriptionShort);

        if (descriptionLong != null)
            tileToUpdate.setDescriptionLong(descriptionLong);

        if (textColor != null)
            tileToUpdate.setTextColor(textColor);

        tileRepository.save(tileToUpdate);
    }

    public Tile getTile(Tileset tileset, int id) {
        Optional<Tile> tileOptional = findTile(tileset, id);

        if (tileOptional.isEmpty())
            throw new NotFoundException(id);

        return tileOptional.get();
    }

    public Optional<Tile> findTile(Tileset tileset, int id) {
        return tileRepository.findByIdAndTileset(id, tileset);
    }

    public byte[] getTileImage(int tileId) {
        Optional<Tile> tile = tileRepository.findById(tileId);

        if (tile.isEmpty())
            throw new NotFoundException(tileId);

        try {
            byte[] result = Files.readAllBytes(tileFileUtil.getTileFilehandler(tile.get()).toPath());
            return result;
        } catch (IOException e) {
            logger.error("Unable to read tile-file.");
            throw new FilesystemException();
        }
    }

    public void delete(int id) throws IOException {
        Tileset target = get(id);
        tilesetRepository.delete(get(id));
        tileSetFileUtil.deleteFile(target.getWorld(), target.getFileName());
        tileFileUtil.deleteDirectory(target.getWorld(), String.valueOf(target.getId()));
    }

    public Tileset add(World world, String name, Integer tileWidth, Integer tileHeight, byte[] image) throws IOException {
        Optional<Tileset> referenceTileset = tilesetRepository.findByWorldAndName(world, name);

        if (referenceTileset.isPresent())
            throw new AlreadyExistingException(name);

        Tileset tilesetNew = new Tileset(world, name, tileWidth, tileHeight);
        tilesetRepository.save(tilesetNew);

        tileSetFileUtil.putFile(world, tilesetNew.getFileName(), image);

        // image
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(image));

        int sizeX = img.getWidth() / tileWidth;
        int sizeY = img.getTileHeight() / tileHeight;
        int i = 0;

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                BufferedImage tileImage = img.getSubimage(
                        x * tileWidth,
                        y * tileWidth,
                        tileWidth,
                        tileHeight
                );

                if (containsContent(tileImage)) {
                    ImageIO.write(
                            tileImage
                            , "png", tileFileUtil.resolve(world, String.format("%d/%d.png", tilesetNew.getId(), i)));

                    Tile tileNew = new Tile(i, tilesetNew);
                    tileRepository.save(tileNew);

                    i++;
                }
            }
        }

        tilesetNew.setnTiles(i+1);
        tilesetRepository.save(tilesetNew);

        return tilesetNew;
    }

    public Tileset get(Integer id) {
        Optional<Tileset> optionalTileset = find(id);

        if (optionalTileset.isEmpty())
            throw new NotFoundException(id);

        return optionalTileset.get();
    }

    public Optional<Tileset> find(Integer id) {
        return tilesetRepository.findById(id);
    }

    public TilesetRepository getTilesetRepository() {
        return tilesetRepository;
    }


    private static boolean containsContent(BufferedImage image) {
        for (int i = 0; i < image.getHeight(); i++)
            for (int j = 0; j < image.getWidth(); j++)
                if (!isTransparent(image, j, i))
                    return true;
        return false;
    }

    private static boolean isTransparent(BufferedImage image, int x, int y) {
        int pixel = image.getRGB(x, y);
        return (pixel >> 24) == 0x00;
    }
}
