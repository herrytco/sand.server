package systems.nope.worldseed.repository.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.StatSheet;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StatSheetRepository extends JpaRepository<StatSheet, Integer> {

    Optional<StatSheet> findByWorldAndName(World world, String name);

    @Transactional
    @Modifying
    void deleteAllByWorldAndName(World world, String name);
}
