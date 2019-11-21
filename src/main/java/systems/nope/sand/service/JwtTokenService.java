package systems.nope.sand.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.stereotype.Service;
import systems.nope.sand.config.SpringUser;
import systems.nope.sand.constants.TokenConstants;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenService implements ITokenService {

    private Key signingKey = Keys.hmacShaKeyFor(TokenConstants.JWT_SECRET.getBytes());

    @Override
    public String generateToken(SpringUser user) {

        return Jwts.builder()
                .signWith(signingKey)
                .setHeaderParam("typ", TokenConstants.TOKEN_TYPE)
                .setIssuer(TokenConstants.TOKEN_ISSUER)
                .setAudience(TokenConstants.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TokenConstants.TOKEN_VALIDITY))
                .setId(String.valueOf(user.getId()))
                .compact();
    }

    @Override
    public String getUsername(String jwtToken) throws SignatureException {
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwtToken).getBody().getSubject();
    }
}
