package systems.nope.worldseed.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.constants.TokenConstants;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenService implements TokenService {

    private Key signingKey = Keys.hmacShaKeyFor(TokenConstants.JWT_SECRET.getBytes());

    private final UserService userService;

    public JwtTokenService(UserService userService) {
        this.userService = userService;
    }


    @Override
    public String generateToken(User user) {
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
    public String extractUsername(String jwtToken) throws SignatureException {
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwtToken).getBody().getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(TokenConstants.JWT_SECRET.getBytes()))
                    .parse(token);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public Authentication authenticate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(TokenConstants.JWT_SECRET.getBytes()))
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();

        UserDetails userDetails = userService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }
}
