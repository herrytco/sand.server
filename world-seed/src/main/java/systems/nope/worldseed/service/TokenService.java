package systems.nope.worldseed.service;

import org.springframework.security.core.Authentication;
import systems.nope.worldseed.model.User;

public interface TokenService {
    String generateToken(User user);

    String extractUsername(String token);

    boolean validateToken(String token);

    Authentication authenticate(String token);
}
