package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.Map;

public interface UserService {
    Map<String,Object> getCurrentUserInfo(String email); // NEW
}
