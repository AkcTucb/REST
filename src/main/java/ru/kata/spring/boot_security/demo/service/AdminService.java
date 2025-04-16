package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    void createUser(UserDTO dto);          // NEW
    void updateUser(Long id, UserDTO dto); // NEW
    void deleteUser(Long id);
    User findById(Long id);
}