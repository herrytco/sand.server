package systems.nope.worldseed.stat.sheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import systems.nope.worldseed.stat.sheet.StatValueConstant;
import systems.nope.worldseed.world.World;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StatValueConstantRepository extends JpaRepository<StatValueConstant, Integer> {
    Optional<StatValueConstant> findByWorldAndNameAndSheet(World world, String name, StatSheet sheet);

    @Transactional
    @Modifying
    void deleteAllByWorldAndName(World world, String name);
}
