package net.dunice.features.auth.services;

import net.dunice.features.auth.dtos.requests.LoginRequest;
import net.dunice.features.auth.dtos.requests.RegisterRequest;
import net.dunice.features.auth.dtos.responses.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    UserResponse registerUser(RegisterRequest request);

    UserResponse loginUser(LoginRequest request);

    UserDetails loadCurrentAuth();

    void deleteCurrentAuth();
}
