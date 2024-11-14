package net.dunice.newsapi.services;

import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.DataResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;
import java.util.UUID;

public interface UserAuthService extends UserDetailsService {
    DataResponse<List<PublicUserResponse>> loadAllUsers();

    PublicUserResponse loadUserByUuid(UUID uuid);

    AuthUserResponse loadByEmail(String email);

    DataResponse<PublicUserResponse> updateUser(UUID uuid, UpdateUserRequest request);

    AuthUserResponse registerUser(RegisterRequest request);

    AuthUserResponse loginUser(LoginRequest request);

    AbstractAuthenticationToken generateAuthToken(String token);

    void deleteUserByUsername(String username);
}
