package systems.nope.worldseed.stat.sheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import systems.nope.worldseed.stat.sheet.StatSheet;
import systems.nope.worldseed.world.World;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StatSheetRepository extends JpaRepository<StatSheet, Integer> {

    Optional<StatSheet> findByWorldAndName(World world, String name);

    @Transactional
    @Modifying
    void deleteAllByWorldAndName(World world, String name);
}
