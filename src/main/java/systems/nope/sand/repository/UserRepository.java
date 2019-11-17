package systems.nope.sand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.sand.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String saltedPassword);
}
