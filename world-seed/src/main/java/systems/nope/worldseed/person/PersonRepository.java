package systems.nope.worldseed.person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByName(String name);

    Optional<Person> findByApiKey(String apiKey);

}
