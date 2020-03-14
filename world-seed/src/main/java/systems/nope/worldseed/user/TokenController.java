package systems.nope.worldseed.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.user.requests.TokenRequest;
import systems.nope.worldseed.user.responses.TokenResponse;

@RestController
@RequestMapping("/tokens")
public class TokenController {
    public TokenController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> token(
            @RequestBody TokenRequest tokenRequest
    ) {
        try {
            UserDetails requestingUser = userService.loadUserByUsername(tokenRequest.username);

            if (requestingUser instanceof User) {
                User user = (User) requestingUser;
                return ResponseEntity.ok(
                        new TokenResponse(tokenService.generateToken(user),
                                user.getName(),
                                user.getEmail()
                        )
                );
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username not found!");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
