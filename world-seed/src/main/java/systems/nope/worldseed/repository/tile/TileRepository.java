package systems.nope.worldseed.repository.tile;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.tile.Tile;
import systems.nope.worldseed.model.tile.Tileset;

import java.util.Optional;

public interface TileRepository extends JpaRepository<Tile, Integer> {
    Optional<Tile> findByIdAndTileset(Integer id, Tileset tileset);
}
