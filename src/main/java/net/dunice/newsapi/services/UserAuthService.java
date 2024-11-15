package net.dunice.newsapi.services;

import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserAuthService extends UserDetailsService {
    List<PublicUserResponse> loadAllUsers();

    PublicUserResponse loadUserByUuid(String uuid);

    AuthUserResponse loadByEmail(String email);

    PublicUserResponse updateUser(String uuid, UpdateUserRequest request);

    AuthUserResponse registerUser(RegisterRequest request);

    AuthUserResponse loginUser(LoginRequest request);

    AbstractAuthenticationToken generateAuthToken(String token);

    void deleteUserByUsername(String username);
}
