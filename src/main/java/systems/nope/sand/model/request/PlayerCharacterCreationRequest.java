package systems.nope.sand.model.request;

public class PlayerCharacterCreationRequest {
    private String name;
    private String bodyInfo;
    private String bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBodyInfo() {
        return bodyInfo;
    }

    public void setBodyInfo(String bodyInfo) {
        this.bodyInfo = bodyInfo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
