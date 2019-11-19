package systems.nope.sand.service;

import systems.nope.sand.config.SpringUser;

public interface ITokenService {
    String generateToken(SpringUser user);
}
