package systems.nope.worldseed.repository.tile;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.tile.Tile;

public interface TileRepository extends JpaRepository<Tile, Integer> {
}
