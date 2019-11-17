package systems.nope.sand.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import systems.nope.sand.exceptions.ObjectAlreadyExistsException;
import systems.nope.sand.exceptions.ObjectNotFoundException;
import systems.nope.sand.model.User;
import systems.nope.sand.repository.UserRepository;

import java.util.Optional;

@Component
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ITokenService tokenService;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, ITokenService tokenService, ITokenService tokenService1, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService1;
        this.encoder = encoder;
    }

    public String getUserToken(String email, String password) throws ObjectNotFoundException {
        User user = getUser(email, password);

        return tokenService.generateToken(user.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .roles("USER")
                    .password(user.getPassword());

            return builder.build();
        }

        throw new UsernameNotFoundException(String.format("No active user with the email '%s' was found", email));
    }

    /**
     * @param email    - email the account was registered with
     * @param password - unhashed password
     * @return User from the DB
     * @throws ObjectNotFoundException - if no user was found for the given data
     */
    public User getUser(String email, String password) throws ObjectNotFoundException {
        System.out.println(email);
        System.out.println(encoder.encode(password));

        Optional<User> targetUser = userRepository.findByEmailAndPassword(email, encoder.encode(password));

        if (!targetUser.isPresent())
            throw new ObjectNotFoundException();

        return targetUser.get();
    }

    public void addUser(String email, String password, String username) throws ObjectAlreadyExistsException {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent())
            throw new ObjectAlreadyExistsException();

        User userNew = new User(email, encoder.encode(password), username);
        userRepository.save(userNew);
    }
}
