package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;          // +++

    @Autowired
    public AdminServiceImpl(UserDao userDao,
                            BCryptPasswordEncoder passwordEncoder,
                            RoleService roleService) {          // +++
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;             // +++
    }

    @Override @Transactional(readOnly = true)
    public List<User> getAllUsers() { return userDao.getAllUsers(); }

    /* ---------- логика, переехавшая из контроллера ---------- */

    @Override @Transactional
    public void createUser(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(roleService.findRolesByName(dto.getRoleNames()));
        userDao.saveUser(user);
    }

    @Override @Transactional
    public void updateUser(Long id, UserDTO dto) {
        User user = userDao.getUserById(id);
        if (user == null) throw new IllegalArgumentException("No user id=" + id);

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setRoles(roleService.findRolesByName(dto.getRoleNames()));
        userDao.updateUser(user);
    }

    @Override @Transactional
    public void deleteUser(Long id) { userDao.deleteUser(id); }

    @Override
    public User findById(Long id) { return userDao.findById(id).orElse(null); }
}


