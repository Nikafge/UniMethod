package com.example.unimethod.service;

import com.example.unimethod.dto.RegisterRequest;
import com.example.unimethod.model.Role;
import com.example.unimethod.model.User;
import com.example.unimethod.model.UserStatus;
import com.example.unimethod.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validateRegistration(RegisterRequest request, BindingResult bindingResult) {
        if (request.getUsername() != null && userRepository.existsByUsername(request.getUsername())) {
            bindingResult.rejectValue(
                    "username",
                    "username.duplicate",
                    "Користувач із таким логіном уже існує"
            );
        }

        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            bindingResult.rejectValue(
                    "email",
                    "email.duplicate",
                    "Користувач із таким email уже існує"
            );
        }
    }

    @Transactional
    public void registerTeacher(RegisterRequest request) {
        User user = new User();

        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());

        user.setRole(Role.TEACHER);
        user.setStatus(UserStatus.PENDING);

        userRepository.save(user);
    }

    public List<User> findPendingUsers() {
        return userRepository.findByStatus(UserStatus.PENDING);
    }

    public List<User> findActiveTeachers() {
        return userRepository.findByRoleAndStatus(Role.TEACHER, UserStatus.ACTIVE);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void approveUser(Long id) {
        User user = findById(id);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Transactional
    public void rejectUser(Long id) {
        User user = findById(id);
        user.setStatus(UserStatus.REJECTED);
        userRepository.save(user);
    }


    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));
    }
    @Transactional
    public void deleteUser(Long userId) {
        User user = findById(userId);

        if (user.getRole() != null && user.getRole().name().equals("ADMIN")) {
            long adminsCount = userRepository.findAll().stream()
                    .filter(u -> u.getRole() != null && u.getRole().name().equals("ADMIN"))
                    .count();

            if (adminsCount <= 1) {
                throw new RuntimeException("Не можна видалити останнього адміністратора");
            }
        }

        userRepository.delete(user);
    }
    @Transactional
    public void updateStatus(Long userId, UserStatus status) {
        User user = findById(userId);
        user.setStatus(status);
        userRepository.save(user);
    }
}