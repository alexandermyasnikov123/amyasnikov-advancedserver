package net.dunice.features.users.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.core.dtos.utils.ResponseUtils;
import net.dunice.features.users.clients.AuthApiClient;
import net.dunice.features.users.dtos.requests.UserRequest;
import net.dunice.features.users.dtos.responses.CredentialsResponse;
import net.dunice.features.users.dtos.responses.UserResponse;
import net.dunice.features.users.entities.UserEntity;
import net.dunice.features.users.kafka.UserImageProducer;
import net.dunice.features.users.mappers.UserMapper;
import net.dunice.features.users.repositories.UsersRepository;
import net.dunice.features.users.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository repository;

    private final UserMapper mapper;

    private final AuthApiClient authApiClient;

    private final UserImageProducer imageProducer;

    @Override
    public List<UserResponse> loadAllUsers() {
        return repository.findAll().stream()
                .map(mapper::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse loadCurrent(HttpHeaders headers) {
        CredentialsResponse credentials = ResponseUtils.tryExtractData(authApiClient.loadCurrentPrincipal(headers));
        UserEntity entity = repository
                .findById(credentials.uuid())
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));

        return mapper.mapToResponse(entity);
    }

    @Override
    public UserResponse loadUserByUuid(UUID uuid) {
        UserEntity entity = repository
                .findById(uuid)
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));

        return mapper.mapToResponse(entity);
    }

    @Override
    public UserResponse updateUser(HttpHeaders headers, UserRequest request) {
        UserResponse original = loadCurrent(headers);
        final String oldAvatar = original.avatar();
        UserEntity entity = mapper.mapToEntity(original.id(), request);

        repository.save(entity);

        if (!oldAvatar.equals(entity.getAvatar())) {
            imageProducer.produceImageDeletion(oldAvatar);
        }

        return mapper.mapToResponse(entity);
    }

    @Override
    public UserResponse insertUser(UserRequest request) {
        String username = request.name();
        String email = request.email();

        if (repository.findByEmail(email).isPresent() ||
                repository.findByUsername(username).isPresent()) {
            throw new ErrorCodesException(ErrorCodes.USER_ALREADY_EXISTS);
        }

        UserEntity result = repository.save(mapper.mapToEntity(null, request));
        return mapper.mapToResponse(result);
    }

    @Override
    @Transactional
    public void deleteUser(HttpHeaders headers) {
        UserResponse current = loadCurrent(headers);

        repository.deleteById(current.id());
        imageProducer.produceImageDeletion(current.avatar());
    }

    @Override
    @Transactional
    public void deleteUserByUsername(String username) {
        UserEntity entity = repository
                .findByUsername(username)
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));

        repository.delete(entity);
        imageProducer.produceImageDeletion(entity.getAvatar());
    }
}
