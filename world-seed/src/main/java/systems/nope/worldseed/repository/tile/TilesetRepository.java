package systems.nope.worldseed.repository.tile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import systems.nope.worldseed.model.tile.Tileset;
import systems.nope.worldseed.model.World;

import java.util.Optional;

@Repository
public interface TilesetRepository extends JpaRepository<Tileset, Integer> {
    Optional<Tileset> findByWorldAndName(World world, String name);
}
