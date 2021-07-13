package systems.nope.worldseed.dto.request;


public class NewWorldRequest extends AddNamedResourceRequest {
    public String description;

    public NewWorldRequest() {
    }

    public NewWorldRequest(String name, String description) {
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
