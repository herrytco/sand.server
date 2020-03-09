package systems.nope.worldseed.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.user.requests.TokenRequest;

@RestController
@RequestMapping("/tokens")
public class TokenController {
    public TokenController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    private final TokenService tokenService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> token(
            @RequestBody TokenRequest tokenRequest
    ) {
        try {
            UserDetails requestingUser = userService.loadUserByUsername(tokenRequest.username);

            if (requestingUser instanceof User) {
                User user = (User) requestingUser;
                return ResponseEntity.ok(tokenService.generateToken(user));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username not found!");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
