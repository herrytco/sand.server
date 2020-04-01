package systems.nope.worldseed.article;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.category.Category;
import systems.nope.worldseed.document.Document;
import systems.nope.worldseed.document.DocumentService;
import systems.nope.worldseed.world.World;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final DocumentService documentService;

    public ArticleService(ArticleRepository articleRepository, DocumentService documentService) {
        this.articleRepository = articleRepository;
        this.documentService = documentService;
    }

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    public Article add(World world, Category category, String name, String content) {
        if (articleRepository.findByTitle(name).isPresent())
            throw new IllegalStateException(String.format("Duplicate Article with title %s", name));

        Document articleDocument = documentService.add(world, content);

        Article article = new Article(world, category, articleDocument, name);

        articleRepository.save(article);

        return article;
    }
}
