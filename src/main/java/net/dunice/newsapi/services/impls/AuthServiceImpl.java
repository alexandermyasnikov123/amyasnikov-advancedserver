package net.dunice.newsapi.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.security.JwtService;
import net.dunice.newsapi.services.AuthService;
import net.dunice.newsapi.services.UserService;
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

    private final UserEntityMapper mapper;

    private final UserService userService;

    @Override
    public AuthUserResponse registerUser(RegisterRequest request) {
        RegisterRequest encodedRequest = request.withEncodedPassword(encoder);
        PublicUserResponse response = userService.insertUser(mapper.registerRequestToEntity(encodedRequest));

        String token = jwtService.generateTokenWithHeader(
                response.name(),
                response.role(),
                response.id()
        );

        return mapper.publicResponseToAuth(response, token);
    }

    @Override
    public AuthUserResponse loginUser(LoginRequest request) {
        PublicUserResponse response = userService.loadByEmail(request.email());
        String token = jwtService.generateTokenWithHeader(
                response.name(),
                response.role(),
                response.id()
        );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(response.name(), request.password())
        );

        return mapper.publicResponseToAuth(response, token);
    }
}
