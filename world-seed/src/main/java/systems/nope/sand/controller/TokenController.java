package systems.nope.sand.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import systems.nope.sand.config.SpringUser;
import systems.nope.sand.model.request.LoginRequest;
import systems.nope.sand.model.responses.TokenResponse;
import systems.nope.sand.service.ITokenService;
import systems.nope.sand.service.UserService;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    public TokenController(UserService userService, AuthenticationManager authenticationManager, ITokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final ITokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenResponse> getToken(
            @RequestBody LoginRequest request
    ) {
        try {
            authenticate(request.getEmail(), request.getPassword());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }

        SpringUser userDetails = userService.loadUserByUsername(request.getEmail());
        return ResponseEntity.ok(new TokenResponse(tokenService.generateToken(userDetails)));
    }

    private void authenticate(String username, String password) throws BadCredentialsException, DisabledException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw e;
        } catch (DisabledException e) {
            throw e;
        }
    }
}
