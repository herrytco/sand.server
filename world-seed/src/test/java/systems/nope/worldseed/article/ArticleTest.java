package systems.nope.worldseed.article;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import systems.nope.worldseed.model.Article;
import systems.nope.worldseed.service.ArticleService;
import systems.nope.worldseed.user.UserTestUtil;
import systems.nope.worldseed.world.WorldTestUtil;
import systems.nope.worldseed.dto.request.CreateArticleRequest;
import systems.nope.worldseed.dto.request.UpdateArticleRequest;
import systems.nope.worldseed.model.Category;
import systems.nope.worldseed.model.World;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleTest {
    @Autowired
    private UserTestUtil userTestUtil;

    @Autowired
    private WorldTestUtil worldinator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private ArticleService articleService;

    @BeforeEach
    public void ensureData() {
        userTestUtil.ensureTestuserExists();
        worldinator.ensureTestWorldExists();
        articleService.getArticleRepository().deleteByTitle(ArticleConstants.articleName);
    }

    @AfterEach
    public void wipeArticle() {
        articleService.getArticleRepository().deleteByTitle(ArticleConstants.articleName);
        articleService.getArticleRepository().deleteByTitle(ArticleConstants.articleName2);
    }

    @Test
    public void updateArticleTest() throws Exception {
        World testWorld = worldinator.ensureTestWorldExists();
        String token = userTestUtil.authenticateTestUser();

        assert testWorld.getCategories().size() > 0;

        Category category = testWorld.getCategories().get(0);

        CreateArticleRequest createArticleRequest = new CreateArticleRequest(
                ArticleConstants.articleName,
                category.getId(),
                ArticleConstants.articleContent
        );

        MvcResult result = mockMvc.perform(
                post(String.format("/articles/world/%d", testWorld.getId()))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(builder.build().writeValueAsString(createArticleRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Article addedArticle = builder.build().readerFor(Article.class).readValue(result.getResponse().getContentAsString());

        Integer id = addedArticle.getId();

        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest(
                category.getId(),
                ArticleConstants.articleName2,
                ArticleConstants.articleContent + "-" + ArticleConstants.articleContent
        );

        // update request
        mockMvc.perform(
                post(String.format("/articles/id/%d", id))
                        .header("Authorization", String.format("Bearer %s", token))
                        .content(builder.build().writeValueAsString(updateArticleRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addArticleTest() throws Exception {
        World testWorld = worldinator.ensureTestWorldExists();
        String token = userTestUtil.authenticateTestUser();

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
