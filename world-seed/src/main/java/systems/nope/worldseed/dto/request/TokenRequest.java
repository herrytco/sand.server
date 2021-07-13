package systems.nope.worldseed.dto.request;

import lombok.Data;

@Data
public class TokenRequest {
    public String username;
    public String password;

    public TokenRequest() {
    }

    public TokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
