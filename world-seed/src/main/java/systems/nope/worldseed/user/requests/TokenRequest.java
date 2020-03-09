package systems.nope.worldseed.user.requests;

public class TokenRequest {
    public final String username;
    public final String password;

    public TokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
