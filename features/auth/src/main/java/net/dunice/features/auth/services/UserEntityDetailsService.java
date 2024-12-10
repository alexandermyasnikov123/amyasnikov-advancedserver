package net.dunice.features.auth.services;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserEntityDetailsService extends UserDetailsService {
    void deleteUserByUsername(String username);
}
