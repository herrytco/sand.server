package systems.nope.worldseed.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.stat.model.StatValueInstanceConstant;

public interface StatValueInstanceConstantRepository extends JpaRepository<StatValueInstanceConstant, Integer> {
}
