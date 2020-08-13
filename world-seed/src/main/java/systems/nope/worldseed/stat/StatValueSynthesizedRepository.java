package systems.nope.worldseed.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.stat.model.StatValueSynthesized;
import systems.nope.worldseed.world.World;

import java.util.Optional;

public interface StatValueSynthesizedRepository extends JpaRepository<StatValueSynthesized, Integer> {
    Optional<StatValueSynthesized> findByWorldAndName(World world, String name);
}
