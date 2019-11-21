package systems.nope.sand.model.request;

public class WorldAddRequest {
    private String name;
    private String description;
    private String worldAnvilLink;

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

    public String getWorldAnvilLink() {
        return worldAnvilLink;
    }

    public void setWorldAnvilLink(String worldAnvilLink) {
        this.worldAnvilLink = worldAnvilLink;
    }
}
