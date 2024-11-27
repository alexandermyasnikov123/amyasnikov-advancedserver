package net.dunice.newsapi.services.impls;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.repositories.UsersRepository;
import net.dunice.newsapi.services.UserService;
import net.dunice.newsapi.utils.AuthenticationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    UsersRepository repository;

    UserEntityMapper mapper;

    @Override
    public List<PublicUserResponse> loadAllUsers() {
        return repository.findAll().stream()
                .map(mapper::entityToPublicResponse)
                .toList();
    }

    @Override
    public PublicUserResponse loadUserByUuid(String uuid) {
        UserEntity entity = repository.findById(UUID.fromString(uuid)).orElseThrow(this::getUserNotFoundException);

        return mapper.entityToPublicResponse(entity);
    }

    @Override
    public PublicUserResponse loadByEmail(String email) {
        UserEntity user = repository.findUserEntityByEmail(email).orElseThrow(this::getUserNotFoundException);
        return mapper.entityToPublicResponse(user);
    }

    @Override
    public PublicUserResponse updateUser(String uuid, UpdateUserRequest request) {
        UUID mappedUuid = UUID.fromString(uuid);
        UserEntity original = repository.findById(mappedUuid).orElseThrow(this::getUserNotFoundException);
        UserEntity entity = repository.save(mapper.updateRequestToEntity(mappedUuid, original.getPassword(), request));

        return mapper.entityToPublicResponse(entity);
    }

    @Override
    public PublicUserResponse insertUser(UserEntity entity) {
        String username = entity.getUsername();
        String email = entity.getEmail();

        if (hasUserWithEmail(email) || hasUserWithUsername(username)) {
            throw new ErrorCodesException(ErrorCodes.USER_ALREADY_EXISTS);
        }

        UserEntity result = repository.save(entity);
        return mapper.entityToPublicResponse(result);
    }

    @Transactional
    @Override
    public void deleteUser(Authentication authentication) {
        UserEntity user = AuthenticationUtils.getUser(authentication);
        UUID userId = user.getId();

        if (!hasUserWithId(userId)) {
            throw getUserNotFoundException();
        }
        repository.deleteById(userId);
    }

    private Boolean hasUserWithEmail(String email) {
        return repository.findUserEntityByEmail(email).isPresent();
    }

    private Boolean hasUserWithUsername(String username) {
        return repository.findUserEntityByUsername(username).isPresent();
    }

    private Boolean hasUserWithId(UUID id) {
        return repository.findById(id).isPresent();
    }

    private RuntimeException getUserNotFoundException() {
        return new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);
    }
}
