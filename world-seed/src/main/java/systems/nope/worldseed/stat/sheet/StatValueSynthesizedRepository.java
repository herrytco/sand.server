package systems.nope.worldseed.stat.sheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import systems.nope.worldseed.stat.sheet.StatValueSynthesized;
import systems.nope.worldseed.world.World;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StatValueSynthesizedRepository extends JpaRepository<StatValueSynthesized, Integer> {
    Optional<StatValueSynthesized> findByWorldAndNameAndSheet(World world, String name, StatSheet sheet);

    @Transactional
    @Modifying
    void deleteAllByWorldAndName(World world, String name);
}
