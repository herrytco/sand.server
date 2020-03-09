package systems.nope.worldseed.person_access;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonAccessRepository extends JpaRepository<PersonAccess, Integer> {
}
