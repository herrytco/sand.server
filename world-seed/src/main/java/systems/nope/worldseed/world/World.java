package systems.nope.worldseed.world;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import systems.nope.worldseed.article.Article;
import systems.nope.worldseed.category.Category;
import systems.nope.worldseed.person.Person;
import systems.nope.worldseed.stat.sheet.StatSheet;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class World {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    private String name;

    private String description;

    @OneToMany(mappedBy = "world")
    private List<Person> persons;

    @OneToMany(mappedBy = "world", fetch = FetchType.EAGER)
    private List<Category> categories;

    @OneToMany(mappedBy = "world")
    private List<StatSheet> sheets;

    @NotNull
    String seed;

    @OneToMany
    @JoinColumn(name = "world")
    List<Article> articles;

    public World() {
    }

    public World(String name, String description, String seed) {
        this.name = name;
        this.description = description;
        this.seed = seed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<StatSheet> getSheets() {
        return sheets;
    }

    public void setSheets(List<StatSheet> sheets) {
        this.sheets = sheets;
    }
}
