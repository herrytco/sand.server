package systems.nope.worldseed.repository.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstanceSynthesized;

public interface StatValueInstanceSynthesizedRepository extends JpaRepository<StatValuePersonInstanceSynthesized, Integer> {
}
