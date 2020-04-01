package systems.nope.worldseed.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import systems.nope.worldseed.Authenticator;
import systems.nope.worldseed.Worldinator;
import systems.nope.worldseed.article.requests.CreateArticleRequest;
import systems.nope.worldseed.category.Category;
import systems.nope.worldseed.world.World;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleTest {
    @Autowired
    private Authenticator authenticator;

    @Autowired
    private Worldinator worldinator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private ArticleService articleService;

    @BeforeEach
    public void ensureData() {
        authenticator.ensureTestuserExists();
        worldinator.ensureTestWorldExists();
        articleService.getArticleRepository().deleteByTitle(ArticleConstants.articleName);
    }

    @Test
    public void addArticleTest() throws Exception {
        World testWorld = worldinator.ensureTestWorldExists();
        String token = authenticator.authenticateTestUser();

        assert testWorld.getCategories().size() > 0;

        Category category = testWorld.getCategories().get(0);

        CreateArticleRequest createArticleRequest = new CreateArticleRequest(
                ArticleConstants.articleName,
                category.getId(),
                ArticleConstants.articleContent
        );

        mockMvc.perform(
                post(String.format("/articles/world/%d", testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(builder.build().writeValueAsString(createArticleRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }
}
