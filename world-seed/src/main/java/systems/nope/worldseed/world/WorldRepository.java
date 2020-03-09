package systems.nope.worldseed.world;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface WorldRepository extends JpaRepository<World, Integer> {
    Optional<World> findByName(String name);

    @Transactional
    void deleteAllByName(String name);
}
