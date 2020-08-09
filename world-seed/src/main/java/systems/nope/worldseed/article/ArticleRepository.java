package systems.nope.worldseed.article;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.world.World;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findByTitle(String title);

    @Transactional
    void deleteByTitle(String title);

    List<Article> findAllByWorld(World world);
}
