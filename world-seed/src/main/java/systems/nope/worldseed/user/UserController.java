package systems.nope.worldseed.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.user.requests.RegistrationRequest;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/id/{id}")
    public User one(
            @PathVariable int id
    ) {
        return userService.getUserRepository().getOne(id);
    }

    @PostMapping
    public ResponseEntity<?> add(
            @RequestBody RegistrationRequest request
    ) {
        System.out.println(String.format("UserAddRequest %s", request));
        if (userService.addUser(request.name, request.email, request.password)) {
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
