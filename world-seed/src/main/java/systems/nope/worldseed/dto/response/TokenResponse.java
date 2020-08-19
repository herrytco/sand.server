package systems.nope.worldseed.dto.response;

public class TokenResponse {
    private int id;

    private String token;

    private String name;

    private String email;

    public TokenResponse() {
    }

    public TokenResponse(int id, String token, String name, String email) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
