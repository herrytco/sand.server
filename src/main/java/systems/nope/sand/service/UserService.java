package systems.nope.sand.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import systems.nope.sand.config.SpringUser;
import systems.nope.sand.exceptions.ObjectAlreadyExistsException;
import systems.nope.sand.model.User;
import systems.nope.sand.repository.UserRepository;

import java.util.Optional;

@Component
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SpringUser loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return new SpringUser(user.getId(), user.getEmail(), user.getPassword());
        }

        throw new UsernameNotFoundException(String.format("No active user with the email '%s' was found", email));
    }

    public void addUser(String email, String password, String username) throws ObjectAlreadyExistsException {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent())
            throw new ObjectAlreadyExistsException();

        User userNew = new User(email, encoder.encode(password), username);
        userRepository.save(userNew);
    }
}
