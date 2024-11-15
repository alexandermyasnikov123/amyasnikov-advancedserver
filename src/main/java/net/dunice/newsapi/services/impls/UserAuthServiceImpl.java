package net.dunice.newsapi.services.impls;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.DataResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.repositories.UsersRepository;
import net.dunice.newsapi.security.JwtService;
import net.dunice.newsapi.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAuthServiceImpl implements UserAuthService {
    UsersRepository repository;

    UserEntityMapper mapper;

    JwtService jwtService;

    PasswordEncoder encoder;

    @NonFinal
    @Setter(onMethod_ = {@Autowired, @Lazy})
    AuthenticationManager authenticationManager;

    @Override
    public List<PublicUserResponse> loadAllUsers() {
        return repository.findAll().stream()
                .map(mapper::entityToPublicResponse)
                .toList();
    }

    @Override
    public PublicUserResponse loadUserByUuid(UUID uuid) {
        UserEntity entity = repository.findById(uuid).orElseThrow(() ->
                new ErrorCodesException(ErrorCodes.USER_NOT_FOUND)
        );
        return mapper.entityToPublicResponse(entity);
    }

    @Override
    public AuthUserResponse loadByEmail(String email) {
        UserEntity user = repository.findUserEntityByEmail(email).orElseThrow(() ->
                new ErrorCodesException(ErrorCodes.USER_NOT_FOUND)
        );

        String token = jwtService.generateToken(user.getUsername(), user.getRole(), user.getUuid());
        return mapper.entityToUserResponse(user, token);
    }

    @Override
    @Transactional
    public DataResponse<PublicUserResponse> updateUser(UUID uuid, UpdateUserRequest request) {
        UserEntity original = repository.findById(uuid).orElseThrow(() ->
                new ErrorCodesException(ErrorCodes.USER_NOT_FOUND)
        );

        UserEntity entity = repository.save(mapper.updateRequestToEntity(uuid, original.getPassword(), request));
        PublicUserResponse response = mapper.entityToPublicResponse(entity);

        return new DataResponse<>(response);
    }

    @Override
    @Transactional
    public void deleteUserByUsername(String username) {
        repository.deleteUserEntityByUsername(username);
    }

    @Override
    @Transactional
    public AuthUserResponse registerUser(RegisterRequest request) {
        Supplier<Boolean> hasUserWithUsername = () -> repository.findUserEntityByUsername(request.name()).isPresent();
        Supplier<Boolean> hasUserWithEmail = () -> repository.findUserEntityByEmail(request.email()).isPresent();

        if (hasUserWithUsername.get() || hasUserWithEmail.get()) {
            throw new ErrorCodesException(ErrorCodes.USER_ALREADY_EXISTS);
        }

        UserEntity user = repository.save(mapper.registerRequestToEntity(request, encoder));
        String token = jwtService.generateToken(user.getUsername(), user.getRole(), user.getUuid());

        return mapper.entityToUserResponse(user, token);
    }

    @Override
    public AuthUserResponse loginUser(LoginRequest request) {
        AuthUserResponse response = loadByEmail(request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(response.name(), request.password())
        );

        String token = jwtService.generateToken(response.name(), response.role(), UUID.fromString(response.id()));
        return response.withToken(token);
    }

    @Override
    public AbstractAuthenticationToken generateAuthToken(String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = loadUserByUsername(username);

        Boolean isTokenValid = jwtService.isTokenValid(token, user.getUsername(), user.getRole(), user.getUuid());

        if (!isTokenValid) {
            throw new ErrorCodesException(ErrorCodes.INVALID_JWT_TOKEN);
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public UserEntity loadUserByUsername(String username) {
        return repository.findUserEntityByUsername(username).orElseThrow(() ->
                new ErrorCodesException(ErrorCodes.USER_NOT_FOUND)
        );
    }
}
