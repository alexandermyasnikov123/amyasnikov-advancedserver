package net.dunice.features.auth.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.features.auth.clients.UserApiClient;
import net.dunice.features.auth.dtos.requests.LoginRequest;
import net.dunice.features.auth.dtos.requests.RegisterRequest;
import net.dunice.features.auth.dtos.responses.UserResponse;
import net.dunice.features.auth.security.JwtService;
import net.dunice.features.auth.services.AuthService;
import net.dunice.features.core.dtos.utils.ResponseUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final UserApiClient userApiClient;

    @Override
    public UserResponse registerUser(RegisterRequest request) {
        UserResponse response = ResponseUtils.tryExtractData(
                userApiClient.insertUser(request.withEncodedPassword(encoder))
        );

        String token = jwtService.generateTokenWithHeader(
                response.name(),
                response.role(),
                response.id()
        );

        return response.withToken(token);
    }

    @Override
    public UserResponse loginUser(LoginRequest request) {
        UserResponse response = ResponseUtils.tryExtractData(
                userApiClient.loadByEmail(request.email())
        );

        String token = jwtService.generateTokenWithHeader(
                response.name(),
                response.role(),
                response.id()
        );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(response.name(), request.password())
        );

        return response.withToken(token);
    }
}
