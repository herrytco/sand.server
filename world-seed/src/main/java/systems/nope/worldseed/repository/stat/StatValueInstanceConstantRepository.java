package systems.nope.worldseed.repository.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceConstant;

public interface StatValueInstanceConstantRepository extends JpaRepository<StatValueInstanceConstant, Integer> {
}
