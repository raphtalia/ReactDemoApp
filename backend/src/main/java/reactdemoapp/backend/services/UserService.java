package reactdemoapp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactdemoapp.backend.models.User;
import reactdemoapp.backend.repositories.UserRepository;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // username == email
        User user = userRepository.findByEmail(email);

        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        if (user != null) {
            builder
                    .username(user.getEmail())
                    .password(user.getPassword());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
