package systems.nope.worldseed.user;

import org.springframework.security.core.Authentication;

public interface TokenService {
    String generateToken(User user);

    String extractUsername(String token);

    boolean validateToken(String token);

    Authentication authenticate(String token);
}
