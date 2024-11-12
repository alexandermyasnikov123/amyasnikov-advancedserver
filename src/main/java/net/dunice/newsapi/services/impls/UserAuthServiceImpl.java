package net.dunice.newsapi.services.impls;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.UserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.repositories.UsersRepository;
import net.dunice.newsapi.security.JwtService;
import net.dunice.newsapi.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter(onMethod_ = {@Autowired, @Lazy})
public class UserAuthServiceImpl implements UserAuthService {
    final UsersRepository repository;

    final UserEntityMapper mapper;

    final JwtService jwtService;

    final PasswordEncoder encoder;

    AuthenticationManager authenticationManager;

    @Override
    public UserEntity loadUserByUsername(String username) {
        return repository.findUserEntityByUsername(username).orElseThrow(() ->
                new ErrorCodesException(ErrorCodes.USER_NOT_FOUND)
        );
    }

    @Override
    public UserResponse loadByUsername(String username) {
        UserEntity user = loadUserByUsername(username);
        return mapper.entityToResponse(user, jwtService.generateToken(user.getUsername()));
    }

    @Override
    public UserResponse loadByEmail(String email) {
        UserEntity user = repository.findUserEntityByEmail(email).orElseThrow(() ->
                new ErrorCodesException(ErrorCodes.USER_NOT_FOUND)
        );

        return mapper.entityToResponse(user, jwtService.generateToken(user.getUsername()));
    }

    @Override
    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        Supplier<Boolean> hasUserWithUsername = () -> repository.findUserEntityByUsername(request.name()).isPresent();
        Supplier<Boolean> hasUserWithEmail = () -> repository.findUserEntityByEmail(request.email()).isPresent();

        if (hasUserWithUsername.get() || hasUserWithEmail.get()) {
            throw new ErrorCodesException(ErrorCodes.USER_ALREADY_EXISTS);
        }

        UserEntity user = repository.save(mapper.requestToEntity(request, encoder));
        String token = jwtService.generateToken(user.getUsername());

        return mapper.entityToResponse(user, token);
    }

    @Override
    public UserResponse loginUser(LoginRequest request) {
        UserResponse response = loadByEmail(request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(response.name(), request.password())
        );

        String token = jwtService.generateToken(response.name());
        return response.withToken(token);
    }

    @Override
    public UserResponse loadCurrentUser() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return loadByUsername(username);
    }
}
