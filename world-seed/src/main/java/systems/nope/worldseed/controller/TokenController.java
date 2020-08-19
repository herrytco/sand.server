package systems.nope.worldseed.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.exception.ImpossibleException;
import systems.nope.worldseed.exception.NotFoundException;
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

    @Operation(summary = "Authenticate at the backend to get a JWT token used for authentication at other requests.")
    @PostMapping
    public TokenResponse token(
            @RequestBody TokenRequest tokenRequest
    ) {
        try {
            UserDetails requestingUser = userService.loadUserByUsername(tokenRequest.username);

            if (requestingUser instanceof User) {
                User user = (User) requestingUser;
                return new TokenResponse(
                        user.getId(),
                        tokenService.generateToken(user),
                        user.getName(),
                        user.getEmail()
                );
            }

            throw new ImpossibleException();
        } catch (UsernameNotFoundException e) {
            throw new NotFoundException(tokenRequest.username);
        }
    }
}
