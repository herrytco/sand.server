package systems.nope.worldseed.repository.person;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.person.PersonAccess;

public interface PersonAccessRepository extends JpaRepository<PersonAccess, Integer> {
}
