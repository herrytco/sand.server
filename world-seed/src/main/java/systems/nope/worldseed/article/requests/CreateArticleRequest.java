package systems.nope.worldseed.article.requests;

public class CreateArticleRequest {
    private String name;
    private int categoryId;
    private String content;

    public CreateArticleRequest() {
    }

    public CreateArticleRequest(String name, int categoryId, String content) {
        this.name = name;
        this.categoryId = categoryId;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CreateArticleRequest{" +
                "name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", content='" + content + '\'' +
                '}';
    }
}
