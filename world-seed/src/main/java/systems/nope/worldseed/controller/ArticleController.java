package systems.nope.worldseed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.model.Article;
import systems.nope.worldseed.service.ArticleService;
import systems.nope.worldseed.dto.request.CreateArticleRequest;
import systems.nope.worldseed.dto.request.UpdateArticleRequest;
import systems.nope.worldseed.model.Category;
import systems.nope.worldseed.repository.CategoryRepository;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.WorldService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

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

    @PostMapping("/id/{articleId}")
    public ResponseEntity<?> update(
            @PathVariable int articleId,
            @RequestBody UpdateArticleRequest request
    ) {
        Category category;

        try {
            category = categoryRepository.getOne(request.getCategoryId());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Category with ID '%d' not found.", request.getCategoryId()));
        }

        Article updatedArticle = articleService.update(
                articleId,
                category,
                request.getTitle(),
                request.getContent()
        );

        return ResponseEntity.ok(updatedArticle);
    }

    @PostMapping("/world/{worldId}")
    public ResponseEntity<?> add(
            @PathVariable int worldId,
            @RequestBody CreateArticleRequest request
    ) {
        System.out.println(request);

        Optional<World> optionalWorld = worldService.find(worldId);

        if (optionalWorld.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("World with ID '%d' not found.", worldId));

        World world = optionalWorld.get();


        Optional<Category> optionalCategory = categoryRepository.findById(request.getCategoryId());

        if (optionalCategory.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Category with ID '%d' not found.", request.getCategoryId()));

        Category category = optionalCategory.get();

        Article articleNew = articleService.add(world, category, request.getName(), request.getContent());

        return ResponseEntity.ok(articleService.getArticleRepository().getOne(articleNew.getId()));
    }

}
