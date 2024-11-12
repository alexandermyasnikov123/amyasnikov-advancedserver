package net.dunice.newsapi.services;

import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthService extends UserDetailsService {
    UserResponse loadByUsername(String username);

    UserResponse loadByEmail(String email);

    UserResponse registerUser(RegisterRequest request);

    UserResponse loginUser(LoginRequest request);

    UserResponse loadCurrentUser();
}
