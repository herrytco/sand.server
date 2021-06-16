package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.Action;

public interface ActionRepository extends JpaRepository<Action, Integer> {
}
