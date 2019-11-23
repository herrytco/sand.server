package systems.nope.worldseed.service;

import systems.nope.worldseed.config.SpringUser;

public interface ITokenService {
    String generateToken(SpringUser user);

    String getUsername(String token);

//    boolean validateToken(String token);
}
