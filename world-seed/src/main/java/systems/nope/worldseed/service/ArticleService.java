package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.Category;
import systems.nope.worldseed.model.Document;
import systems.nope.worldseed.model.Article;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.repository.ArticleRepository;

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

    public Article update(int id, Category category, String name, String content) {
        Article targetArticle = articleRepository.getOne(id);

        // update article basedata
        targetArticle.setTitle(name);
        targetArticle.setCategory(category);


        targetArticle.setDocument(
                documentService.update(
                        targetArticle.getDocument(),
                        content
                )
        );
        articleRepository.save(targetArticle);

        return targetArticle;
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
