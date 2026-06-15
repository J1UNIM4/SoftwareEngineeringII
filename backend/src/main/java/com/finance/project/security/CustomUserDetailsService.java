package com.finance.project.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Usuarios precargados del Bootstrapping
    private static final Map<String, String> USERS = new HashMap<>();

    static {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        USERS.put("miguel@gmail.com",   encoder.encode("password123"));
        USERS.put("bernardo@gmail.com", encoder.encode("password123"));
        USERS.put("maria@gmail.com",    encoder.encode("password123"));
        USERS.put("pedro@gmail.com",    encoder.encode("password123"));
        USERS.put("francis@gmail.com",  encoder.encode("password123"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String password = USERS.get(email);
        if (password == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }
        return User.builder()
                .username(email)
                .password(password)
                .roles("USER")
                .build();
    }
}
