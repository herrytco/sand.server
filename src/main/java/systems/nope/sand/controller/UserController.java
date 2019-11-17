package systems.nope.sand.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.sand.exceptions.ObjectAlreadyExistsException;
import systems.nope.sand.model.request.RegisterRequest;
import systems.nope.sand.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            userService.addUser(request.getEmail(), request.getPassword(), request.getUsername());
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (ObjectAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists.");
        }
    }


}
