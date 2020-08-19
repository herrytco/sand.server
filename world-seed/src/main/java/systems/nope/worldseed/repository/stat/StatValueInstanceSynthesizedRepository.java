package systems.nope.worldseed.repository.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceSynthesized;

public interface StatValueInstanceSynthesizedRepository extends JpaRepository<StatValueInstanceSynthesized, Integer> {
}
