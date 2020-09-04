package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.AlreadyExistingException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.Tileset;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.repository.TilesetRepository;
import systems.nope.worldseed.util.file.TileFileUtil;
import systems.nope.worldseed.util.file.TileSetFileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class TilesetService {

    private final TilesetRepository tilesetRepository;
    private final TileFileUtil tileFileUtil;
    private final TileSetFileUtil tileSetFileUtil;

    public TilesetService(TilesetRepository tilesetRepository, TileFileUtil tileFileUtil, TileSetFileUtil tileSetFileUtil) {
        this.tilesetRepository = tilesetRepository;
        this.tileFileUtil = tileFileUtil;
        this.tileSetFileUtil = tileSetFileUtil;
    }

    public void delete(int id) throws IOException {
        Tileset target = get(id);
        tilesetRepository.delete(get(id));
        tileSetFileUtil.deleteFile(target.getWorld(), target.getFileName());
        tileFileUtil.deleteDirectory(target.getWorld(), String.valueOf(target.getId()));
    }

    private static boolean isTransparent(BufferedImage image, int x, int y) {
        int pixel = image.getRGB(x, y);
        return (pixel >> 24) == 0x00;
    }

    private static boolean containsContent(BufferedImage image) {
        for (int i = 0; i < image.getHeight(); i++)
            for (int j = 0; j < image.getWidth(); j++)
                if (!isTransparent(image, j, i))
                    return true;
        return false;
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
                    i++;
                }
            }
        }

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
}
