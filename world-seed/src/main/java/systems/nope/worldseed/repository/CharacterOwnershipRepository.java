package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.CharacterOwnership;

public interface CharacterOwnershipRepository extends JpaRepository<CharacterOwnership, CharacterOwnership.Pk> {
}
