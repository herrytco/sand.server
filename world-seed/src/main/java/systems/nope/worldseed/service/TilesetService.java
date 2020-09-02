package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.AlreadyExistingException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.Tileset;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.repository.TilesetRepository;

import java.util.Optional;

@Service
public class TilesetService {

    private final TilesetRepository tilesetRepository;

    public TilesetService(TilesetRepository tilesetRepository) {
        this.tilesetRepository = tilesetRepository;
    }

    public void delete(int id) {
        tilesetRepository.delete(get(id));
    }

    public Tileset add(World world, String name, Integer tileWidth, Integer tileHeight) {
        Optional<Tileset> referenceTileset = tilesetRepository.findByWorldAndName(world, name);

        if(referenceTileset.isPresent())
            throw new AlreadyExistingException(name);

        Tileset tilesetNew = new Tileset(world, name, tileWidth, tileHeight);
        tilesetRepository.save(tilesetNew);

        return tilesetNew;
    }

    Tileset get(Integer id) {
        Optional<Tileset> optionalTileset = find(id);

        if(optionalTileset.isEmpty())
            throw new NotFoundException(id);

        return optionalTileset.get();
    }

    Optional<Tileset> find(Integer id) {
        return tilesetRepository.findById(id);
    }

    public TilesetRepository getTilesetRepository() {
        return tilesetRepository;
    }
}
