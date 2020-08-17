package systems.nope.worldseed.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.stat.model.StatValueConstant;
import systems.nope.worldseed.world.World;

import java.util.Optional;

public interface StatValueConstantRepository extends JpaRepository<StatValueConstant, Integer> {
    Optional<StatValueConstant> findByWorldAndName(World world, String name);
}
