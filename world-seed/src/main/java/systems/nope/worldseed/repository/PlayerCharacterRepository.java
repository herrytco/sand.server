package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.PlayerCharacter;
import systems.nope.worldseed.model.World;

import java.util.Optional;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Integer> {

    Optional<PlayerCharacter> findByWorldAndName(World world, String name);
}
