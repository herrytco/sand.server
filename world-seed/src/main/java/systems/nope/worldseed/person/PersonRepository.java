package systems.nope.worldseed.person;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.world.World;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByWorldAndName(World world, String name);

    Optional<Person> findByApiKey(String apiKey);

}
