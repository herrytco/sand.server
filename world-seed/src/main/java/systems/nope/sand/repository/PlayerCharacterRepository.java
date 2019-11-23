package systems.nope.sand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.sand.model.PlayerCharacter;
import systems.nope.sand.model.World;

import java.util.Optional;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Integer> {

    Optional<PlayerCharacter> findByWorldAndName(World world, String name);
}
