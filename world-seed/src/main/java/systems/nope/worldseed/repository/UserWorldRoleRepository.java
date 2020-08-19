package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.model.UserWorldRole;

import javax.transaction.Transactional;
import java.util.List;

public interface UserWorldRoleRepository extends JpaRepository<UserWorldRole, UserWorldRole.Pk> {

    List<UserWorldRole> findAllByUser(User user);

    @Transactional
    void deleteAllByUser(User user);

}
