package systems.nope.worldseed.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean addUser(String name, String email, String password) {

        // if a user with this email already exists, return false (as email should be unique)
        Optional<User> reference = userRepository.findByEmail(email);
        if (reference.isPresent())
            return false;

        User userNew = new User(name, email, passwordEncoder.encode(password));

        try {
            userRepository.save(userNew);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalCorrespondingUser = userRepository.findByEmail(email);

        if (optionalCorrespondingUser.isPresent())
            return optionalCorrespondingUser.get();

        throw new UsernameNotFoundException(String.format("Username '%s' not found!", email));
    }
}
