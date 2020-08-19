package systems.nope.worldseed.repository.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.value.StatValueConstant;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StatValueConstantRepository extends JpaRepository<StatValueConstant, Integer> {
    Optional<StatValueConstant> findByWorldAndNameAndSheet(World world, String name, StatSheet sheet);

    @Transactional
    @Modifying
    void deleteAllByWorldAndName(World world, String name);
}
