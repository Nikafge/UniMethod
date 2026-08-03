package com.example.diploma.service;

import com.example.diploma.model.User;
import com.example.diploma.model.UserStatus;
import com.example.diploma.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

@Override
public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {

    System.out.println("CustomUserDetailsService CALLED");
    System.out.println("LOGIN USERNAME = [" + username + "]");

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено"));

    System.out.println("FOUND USER = " + user.getUsername());
    System.out.println("ROLE = " + user.getRole());
    System.out.println("STATUS = " + user.getStatus());
    System.out.println("PASSWORD LENGTH = " + user.getPassword().length());

    if (user.getStatus() != UserStatus.ACTIVE) {
        throw new DisabledException("Обліковий запис ще не підтверджено адміністратором");
    }

    return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
    );
}
}