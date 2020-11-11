package systems.nope.worldseed.dto.request;

public class AddItemRequest extends AddNamedResourceRequest {
    private String description;

    public AddItemRequest() {
    }

    public AddItemRequest(String name, String description) {
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
