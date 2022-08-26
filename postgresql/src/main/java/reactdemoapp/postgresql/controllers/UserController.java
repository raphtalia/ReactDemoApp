package reactdemoapp.postgresql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactdemoapp.postgresql.models.User;
import reactdemoapp.postgresql.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
