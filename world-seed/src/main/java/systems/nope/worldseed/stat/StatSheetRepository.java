package systems.nope.worldseed.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.stat.model.StatSheet;
import systems.nope.worldseed.world.World;

import java.util.Optional;

public interface StatSheetRepository extends JpaRepository<StatSheet, Integer> {

    Optional<StatSheet> findByName(String name);

    Optional<StatSheet> findByWorldAndName(World world, String name);
}
