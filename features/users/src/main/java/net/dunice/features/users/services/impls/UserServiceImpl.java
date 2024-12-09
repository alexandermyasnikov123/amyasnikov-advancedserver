package net.dunice.features.users.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.shared.entities.UserEntity;
import net.dunice.features.users.dtos.requests.UpdateUserRequest;
import net.dunice.features.users.dtos.responses.PublicUserResponse;
import net.dunice.features.users.mappers.PublicUserMapper;
import net.dunice.features.users.repositories.UsersRepository;
import net.dunice.features.users.services.UserService;
import net.dunice.features.users.utils.AuthenticationUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository repository;

    private final PublicUserMapper mapper;

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
    public PublicUserResponse loadCurrentUser() {
        return mapper.entityToPublicResponse(AuthenticationUtils.getCurrentUser());
    }

    @Override
    public PublicUserResponse loadByEmail(String email) {
        UserEntity user = repository
                .findUserEntityByEmail(email)
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));

        return mapper.entityToPublicResponse(user);
    }

    @Override
    public PublicUserResponse updateUser(UpdateUserRequest request) {
        UserEntity original = AuthenticationUtils.getCurrentUser();
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

    @Override
    public void deleteUser() {
        UserEntity user = AuthenticationUtils.getCurrentUser();
        repository.delete(user);
    }
}
