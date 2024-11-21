package net.dunice.newsapi.services;

import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import java.util.Optional;

public interface AuthService {
    AuthUserResponse registerUser(RegisterRequest request);

    AuthUserResponse loginUser(LoginRequest request);

    Optional<AbstractAuthenticationToken> createAuthenticationTokenIfValid(String jwtToken);
}
