package systems.nope.worldseed.repository.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstanceConstant;

public interface StatValueInstanceConstantRepository extends JpaRepository<StatValuePersonInstanceConstant, Integer> {
}
