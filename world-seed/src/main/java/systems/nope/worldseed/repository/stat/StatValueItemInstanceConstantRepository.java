package systems.nope.worldseed.repository.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.stat.instance.item.StatValueItemInstanceConstant;

public interface StatValueItemInstanceConstantRepository extends JpaRepository<StatValueItemInstanceConstant, Integer> {
}
