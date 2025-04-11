package ru.kata.spring.boot_security.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRestController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Получить данные текущего пользователя
    @GetMapping
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("name", user.getName());
        result.put("email", user.getEmail());
        result.put("roles", user.getRoles());

        return ResponseEntity.ok(result);
    }

    // Обновить профиль
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody Map<String, String> body) {
        Long id = Long.valueOf(body.get("id"));
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");

        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setName(name);
        user.setEmail(email);
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userService.update(user);
        return ResponseEntity.ok("Профиль обновлен");
    }

    // Удаление профиля
    @DeleteMapping
    public ResponseEntity<?> deleteUser(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user != null) {
            userService.deleteUser(user.getId());
        }
        return ResponseEntity.ok("Пользователь удалён");
    }
}
