package net.dunice.newsapi.services.impls;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.security.JwtService;
import net.dunice.newsapi.services.AuthService;
import net.dunice.newsapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    JwtService jwtService;

    PasswordEncoder encoder;

    @NonFinal
    @Setter(onMethod_ = {@Autowired, @Lazy})
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
    public Boolean isTokenValid(String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userService.loadUserByUsername(username);

        return jwtService.isTokenValid(token, user.getUsername(), user.getRole(), user.getUuid());
    }

    @Override
    public AbstractAuthenticationToken generateAuthToken(String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userService.loadUserByUsername(username);

        if (!isTokenValid(token)) {
            throw new ErrorCodesException(ErrorCodes.INVALID_JWT_TOKEN);
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    private String generateBearerTokenHeader(String username, String role, UUID uuid) {
        String bearerPrefix = "Bearer ";
        return bearerPrefix + jwtService.generateToken(username, role, uuid);
    }
}
