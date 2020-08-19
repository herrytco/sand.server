package systems.nope.worldseed.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.repository.UserRepository;
import systems.nope.worldseed.exception.AlreadyExistingException;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    public UserRepository getUserRepository() {
        return userRepository;
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public void addUser(String name, String email, String password) throws AlreadyExistingException {

        // if a user with this email already exists, return false (as email should be unique)
        Optional<User> reference = userRepository.findByEmail(email);
        if (reference.isPresent())
            throw new AlreadyExistingException();

        User userNew = new User(name, email, passwordEncoder.encode(password));

        userRepository.save(userNew);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalCorrespondingUser = userRepository.findByEmail(email);

        if (optionalCorrespondingUser.isPresent())
            return optionalCorrespondingUser.get();

        throw new UsernameNotFoundException(String.format("Username '%s' not found!", email));
    }
}
