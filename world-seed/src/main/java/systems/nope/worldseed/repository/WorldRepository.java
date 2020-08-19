package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.World;

import javax.transaction.Transactional;
import java.util.Optional;

public interface WorldRepository extends JpaRepository<World, Integer> {
    Optional<World> findByName(String name);

    Optional<World> findBySeed(String seed);

    @Transactional
    void deleteAllByName(String name);

    @Transactional
    void deleteBySeed(String seed);
}
