package systems.nope.worldseed.world;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.user.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserWorldRoleRepository extends JpaRepository<UserWorldRole, UserWorldRole.Pk> {

    List<UserWorldRole> findAllByUser(User user);

    @Transactional
    void deleteAllByUser(User user);

}
