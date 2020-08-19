package systems.nope.worldseed.dto.request;

public class UpdateArticleRequest {
    private int categoryId;
    private String title;
    private String content;

    public UpdateArticleRequest() {
    }

    public UpdateArticleRequest(int categoryId, String title, String content) {
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
