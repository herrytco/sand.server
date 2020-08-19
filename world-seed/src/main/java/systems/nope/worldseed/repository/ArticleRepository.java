package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.Article;
import systems.nope.worldseed.model.World;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findByTitle(String title);

    @Transactional
    void deleteByTitle(String title);

    List<Article> findAllByWorld(World world);
}
