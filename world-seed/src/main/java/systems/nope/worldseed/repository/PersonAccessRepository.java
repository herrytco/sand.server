package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.PersonAccess;

public interface PersonAccessRepository extends JpaRepository<PersonAccess, Integer> {
}
