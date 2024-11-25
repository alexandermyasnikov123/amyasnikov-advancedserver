package net.dunice.newsapi.services.impls;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.security.JwtService;
import net.dunice.newsapi.services.AuthService;
import net.dunice.newsapi.services.UserService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    JwtService jwtService;

    PasswordEncoder encoder;

    AuthenticationManager authenticationManager;

    UserEntityMapper mapper;

    UserService userService;

    @Transactional
    @Override
    public AuthUserResponse registerUser(RegisterRequest request) {
        PublicUserResponse response = userService.insertUser(mapper.registerRequestToEntity(request, encoder));
        String token = generateBearerTokenHeader(response.name(), response.role(), UUID.fromString(response.id()));

        return mapper.publicResponseToAuth(response, token);
    }

    @Override
    public AuthUserResponse loginUser(LoginRequest request) {
        PublicUserResponse response = userService.loadByEmail(request.email());
        String token = generateBearerTokenHeader(response.name(), response.role(), UUID.fromString(response.id()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(response.name(), request.password())
        );

        return mapper.publicResponseToAuth(response, token);
    }

    @Override
    public Optional<AbstractAuthenticationToken> createAuthenticationTokenIfValid(String jwtToken) {
        String username = jwtService.extractUsername(jwtToken);
        UserEntity user = userService.loadUserByUsername(username);

        Boolean isValid = jwtService.isTokenValid(jwtToken, user.getUsername(), user.getRole(), user.getUuid());
        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        return isValid ? Optional.of(authToken) : Optional.empty();
    }

    private String generateBearerTokenHeader(String username, String role, UUID uuid) {
        String bearerPrefix = "Bearer ";
        return bearerPrefix + jwtService.generateToken(username, role, uuid);
    }
}
