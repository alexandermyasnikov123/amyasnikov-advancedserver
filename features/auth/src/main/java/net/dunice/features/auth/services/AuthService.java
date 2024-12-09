package net.dunice.features.auth.services;

import net.dunice.features.auth.dtos.requests.LoginRequest;
import net.dunice.features.auth.dtos.requests.RegisterRequest;
import net.dunice.features.auth.dtos.responses.AuthUserResponse;

public interface AuthService {
    AuthUserResponse registerUser(RegisterRequest request);

    AuthUserResponse loginUser(LoginRequest request);
}
