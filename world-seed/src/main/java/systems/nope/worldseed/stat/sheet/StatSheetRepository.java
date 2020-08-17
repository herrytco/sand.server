package systems.nope.worldseed.stat.sheet;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.stat.sheet.StatSheet;
import systems.nope.worldseed.world.World;

import java.util.Optional;

public interface StatSheetRepository extends JpaRepository<StatSheet, Integer> {

    Optional<StatSheet> findByName(String name);

    Optional<StatSheet> findByWorldAndName(World world, String name);
}
