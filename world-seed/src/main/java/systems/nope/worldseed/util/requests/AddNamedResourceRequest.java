package systems.nope.worldseed.util.requests;

public class AddNamedResourceRequest {
    private String name;

    public AddNamedResourceRequest() {
    }

    public AddNamedResourceRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
