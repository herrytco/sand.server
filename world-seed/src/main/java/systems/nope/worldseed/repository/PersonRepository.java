package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.World;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByWorldAndName(World world, String name);

    Optional<Person> findByApiKey(String apiKey);

}
