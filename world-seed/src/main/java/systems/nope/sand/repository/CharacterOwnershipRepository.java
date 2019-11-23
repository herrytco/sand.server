package systems.nope.sand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.sand.model.CharacterOwnership;

public interface CharacterOwnershipRepository extends JpaRepository<CharacterOwnership, CharacterOwnership.Pk> {
}
