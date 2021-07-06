package systems.nope.worldseed.repository.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceResource;

public interface StatValueInstanceResourceRepository extends JpaRepository<StatValueInstanceResource, Integer> {
}
