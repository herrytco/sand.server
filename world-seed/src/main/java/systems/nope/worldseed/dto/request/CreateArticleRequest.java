package systems.nope.worldseed.dto.request;

public class CreateArticleRequest extends AddNamedResourceRequest {
    private int categoryId;
    private String content;

    public CreateArticleRequest() {
    }

    public CreateArticleRequest(String name, int categoryId, String content) {
        super(name);
        this.categoryId = categoryId;
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

    @Override
    public String toString() {
        return "CreateArticleRequest{" +
                "name='" + getName() + '\'' +
                ", categoryId=" + categoryId +
                ", content='" + content + '\'' +
                '}';
    }
}
