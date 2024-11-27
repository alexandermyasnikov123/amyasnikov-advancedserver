package net.dunice.newsapi.services.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository repository;

    private final UserEntityMapper mapper;

    @Override
    public List<PublicUserResponse> loadAllUsers() {
        return repository.findAll().stream()
                .map(mapper::entityToPublicResponse)
                .toList();
    }

    @Override
    public PublicUserResponse loadUserByUuid(String uuid) {
        UserEntity entity = repository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));

        return mapper.entityToPublicResponse(entity);
    }

    @Override
    public PublicUserResponse loadCurrentUser(Authentication authentication) {
        return mapper.entityToPublicResponse(AuthenticationUtils.getUser(authentication));
    }

    @Override
    public PublicUserResponse loadByEmail(String email) {
        UserEntity user = repository
                .findUserEntityByEmail(email)
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));

        return mapper.entityToPublicResponse(user);
    }

    @Override
    public PublicUserResponse updateUser(Authentication authentication, UpdateUserRequest request) {
        UserEntity original = AuthenticationUtils.getUser(authentication);
        UserEntity entity = mapper.updateRequestToEntity(original.getId(), original.getPassword(), request);
        repository.save(entity);

        return mapper.entityToPublicResponse(entity);
    }

    @Override
    public PublicUserResponse insertUser(UserEntity entity) {
        String username = entity.getUsername();
        String email = entity.getEmail();

        if (repository.findUserEntityByEmail(email).isPresent() ||
                repository.findUserEntityByUsername(username).isPresent()) {
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

        if (repository.findById(userId).isEmpty()) {
            throw new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);
        }
        repository.deleteById(userId);
    }
}
