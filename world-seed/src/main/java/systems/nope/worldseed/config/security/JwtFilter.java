package systems.nope.worldseed.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import systems.nope.worldseed.service.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JwtFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getTokenFromRequest(request);

        if (StringUtils.hasText(jwt)) {
            if (tokenService.validateToken(jwt))
                SecurityContextHolder.getContext().setAuthentication(tokenService.authenticate(jwt));
        }

        filterChain.doFilter(request, response);

        // reset authentication after everything was executed
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            return token.substring(7);

        return null;
    }
}
