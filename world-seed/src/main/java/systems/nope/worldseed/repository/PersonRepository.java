package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import systems.nope.worldseed.model.Person;
import systems.nope.worldseed.model.World;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByWorldAndName(World world, String name);

    Optional<Person> findByApiKey(String apiKey);

    List<Person> findByWorld(World world);

    @Transactional
    @Modifying
    void deleteAllByName(String name);

}
