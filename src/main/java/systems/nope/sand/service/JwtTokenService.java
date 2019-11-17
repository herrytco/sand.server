package systems.nope.sand.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import systems.nope.sand.constants.TokenConstants;

import java.util.Date;

@Service
public class JwtTokenService implements ITokenService {
    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, Keys.hmacShaKeyFor(TokenConstants.JWT_SECRET.getBytes()))
                .setHeaderParam("typ", TokenConstants.TOKEN_TYPE)
                .setIssuer(TokenConstants.TOKEN_ISSUER)
                .setAudience(TokenConstants.TOKEN_AUDIENCE)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TokenConstants.TOKEN_VALIDITY))
                .compact();
    }
}
