package systems.nope.worldseed.repository.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StatValueSynthesizedRepository extends JpaRepository<StatValueSynthesized, Integer> {
    Optional<StatValueSynthesized> findByWorldAndNameAndSheet(World world, String name, StatSheet sheet);

    @Transactional
    @Modifying
    void deleteAllByWorldAndName(World world, String name);
}
