package systems.nope.worldseed.article;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.article.requests.CreateArticleRequest;
import systems.nope.worldseed.category.Category;
import systems.nope.worldseed.category.CategoryRepository;
import systems.nope.worldseed.world.World;
import systems.nope.worldseed.world.WorldService;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final WorldService worldService;
    private final ArticleService articleService;
    private final CategoryRepository categoryRepository;

    public ArticleController(WorldService worldService, ArticleService articleService, CategoryRepository categoryRepository) {
        this.worldService = worldService;
        this.articleService = articleService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/world/{worldId}")
    public ResponseEntity<?> add(
            @PathVariable int worldId,
            @RequestBody CreateArticleRequest request
    ) {

        System.out.println(request);

        World world;

        try {
            world = worldService.getWorldRepository().getOne(worldId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with ID '%d' not found.", worldId));
        }

        Category category;

        try {
            category = categoryRepository.getOne(request.getCategoryId());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Category with ID '%d' not found.", request.getCategoryId()));
        }

        Article articleNew = articleService.add(world, category, request.getName(), request.getContent());

        return ResponseEntity.ok(articleService.getArticleRepository().getOne(articleNew.getId()));
    }

}
