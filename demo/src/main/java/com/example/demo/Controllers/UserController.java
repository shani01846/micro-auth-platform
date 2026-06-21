package com.example.demo.Controllers;

import com.example.demo.Entities.User;
import com.example.demo.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users") 
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // קבלת כל המשתמשים (בקשה שתגיע מה-Gateway אחרי אישור אבטחה)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
@GetMapping("/by-username/{username}")
public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
    User user = userRepository.findByUsername(username).orElse(null);
    if (user != null) {
        return ResponseEntity.ok(user);
    }
    return ResponseEntity.notFound().build();
}
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> body) {
        User user = new User();
        user.setUsername(body.get("username"));
        user.setPassword(body.get("password"));
        user.setRole(body.get("role"));
        return ResponseEntity.ok(userRepository.save(user));
    }
}