package systems.nope.sand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.sand.model.World;

import java.util.Optional;

public interface WorldRepository extends JpaRepository<World, Integer> {
    Optional<World> findByName(String name);

    Optional<World> findBySeed(String seed);
}
