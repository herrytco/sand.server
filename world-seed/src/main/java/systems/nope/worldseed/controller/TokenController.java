package systems.nope.worldseed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.service.TokenService;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.service.UserService;
import systems.nope.worldseed.dto.request.TokenRequest;
import systems.nope.worldseed.dto.response.TokenResponse;

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
                        new TokenResponse(
                                user.getId(),
                                tokenService.generateToken(user),
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
