package systems.nope.worldseed.user;

public interface TokenService {
    String generateToken(User user);

    String extractUsername(String token);
}
