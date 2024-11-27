package net.dunice.newsapi.services.impls;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.mappers.UserEntityMapperImpl;
import net.dunice.newsapi.repositories.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImplTest {
    List<UserEntity> allUsers = generateUserEntities(10);

    List<PublicUserResponse> publicUserResponses = allUsers.stream()
            .map(entity -> new PublicUserResponse(
                    entity.getAvatar(),
                    entity.getEmail(),
                    entity.getUuid().toString(),
                    entity.getUsername(),
                    entity.getRole()
            )).toList();

    Optional<UserEntity> foundUser = Optional.of(allUsers.getFirst());

    PublicUserResponse foundPublicUser = publicUserResponses.getFirst();

    @NonFinal
    UsersRepository repository;

    @NonFinal
    UserServiceImpl service;

    @BeforeEach
    public void beforeEach() {
        repository = Mockito.mock(UsersRepository.class);
        UserEntityMapper mapper = new UserEntityMapperImpl();

        Mockito.when(repository.findAll()).thenReturn(allUsers);
        Mockito.when(repository.findById(Mockito.any())).thenReturn(foundUser);
        Mockito.when(repository.findUserEntityByEmail(Mockito.any())).thenReturn(foundUser);
        Mockito.when(repository.findUserEntityByUsername(Mockito.any())).thenReturn(foundUser);

        Mockito.when(repository.save(Mockito.any())).thenReturn(foundUser.orElseThrow());

        Mockito.doNothing().when(repository).deleteUserEntityByUsername(Mockito.anyString());

        service = new UserServiceImpl(repository, mapper);
    }

    @Test
    public void loadAllUsersReturnsPublicResponse() {
        List<PublicUserResponse> actual = service.loadAllUsers();

        Assertions.assertIterableEquals(publicUserResponses, actual);
    }

    @Test
    public void loadUserByUuidReturnsRequiredUserIfExists() {
        PublicUserResponse actual = service.loadUserByUuid(UUID.randomUUID().toString());

        Assertions.assertEquals(foundPublicUser, actual);
    }

    @Test
    public void loadNotExistingUserByUuidThrowsErrorCodesException() {
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());
        var expected = new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);

        ErrorCodesException actual = Assertions.assertThrowsExactly(ErrorCodesException.class, () -> {
            service.loadUserByUuid(UUID.randomUUID().toString());
        });

        Assertions.assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
    }

    @Test
    public void loadUserByEmailReturnsRequiredUserIfExists() {
        PublicUserResponse actual = service.loadByEmail(Mockito.anyString());

        Assertions.assertEquals(foundPublicUser, actual);
    }

    @Test
    public void loadNotExistingUserByEmailThrowsErrorCodesException() {
        Mockito.when(repository.findUserEntityByEmail(Mockito.any())).thenReturn(Optional.empty());
        var expected = new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);

        ErrorCodesException actual = Assertions.assertThrowsExactly(ErrorCodesException.class, () -> {
            service.loadByEmail(Mockito.anyString());
        });

        Assertions.assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
    }

    @Test
    public void loadUserByUsernameReturnsRequiredUserIfExists() {
        UserEntity expected = foundUser.orElseThrow();
        UserEntity actual = service.loadUserByUsername(Mockito.anyString());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void loadNotExistingUserByUsernameThrowsErrorCodesException() {
        Mockito.when(repository.findUserEntityByUsername(Mockito.any())).thenReturn(Optional.empty());
        var expected = new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);

        ErrorCodesException actual = Assertions.assertThrowsExactly(ErrorCodesException.class, () -> {
            service.loadUserByUsername(Mockito.anyString());
        });

        Assertions.assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
    }

    @Test
    public void updateUserUpdatesUserIfExistsByUuid() {
        PublicUserResponse actual = service.updateUser(UUID.randomUUID().toString(), UpdateUserRequest.builder().build());

        Assertions.assertEquals(foundPublicUser, actual);
    }

    @Test
    public void updateThrowsExceptionIfUserNotFound() {
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());
        var expected = new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);

        ErrorCodesException actual = Assertions.assertThrowsExactly(ErrorCodesException.class, () -> {
            service.updateUser(UUID.randomUUID().toString(), UpdateUserRequest.builder().build());
        });

        Assertions.assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
    }

    @Test
    public void insertUserInsertsUserIfUsernameAndEmailAreUnique() {
        AtomicBoolean findByEmailWasCalled = new AtomicBoolean(false);
        AtomicBoolean findByUsernameWasCalled = new AtomicBoolean(false);

        Mockito.when(repository.findUserEntityByEmail(Mockito.anyString())).then(invocation -> {
            findByEmailWasCalled.set(true);
            return Optional.empty();
        });

        Mockito.when(repository.findUserEntityByUsername(Mockito.anyString())).then(invocation -> {
            findByUsernameWasCalled.set(true);
            return Optional.empty();
        });

        PublicUserResponse actual = service.insertUser(foundUser.orElseThrow());

        Assertions.assertEquals(foundPublicUser, actual);
        Assertions.assertTrue(findByEmailWasCalled.get());
        Assertions.assertTrue(findByUsernameWasCalled.get());
    }

    @Test
    public void insertUserThrowsErrorCodesExceptionIfEmailAlreadyPresent() {
        AtomicBoolean findByEmailWasCalled = new AtomicBoolean(false);
        var expected = new ErrorCodesException(ErrorCodes.USER_ALREADY_EXISTS);

        Mockito.when(repository.findUserEntityByEmail(Mockito.anyString())).then(invocation -> {
            findByEmailWasCalled.set(true);
            return foundUser;
        });

        Mockito.when(repository.findUserEntityByUsername(Mockito.anyString())).then(ignored -> Assertions.fail());

        ErrorCodesException actual = Assertions.assertThrowsExactly(ErrorCodesException.class, () -> {
            service.insertUser(foundUser.orElseThrow());
        });

        Assertions.assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
        Assertions.assertTrue(findByEmailWasCalled.get());
    }

    @Test
    public void insertUserThrowsErrorCodesExceptionIfUsernameAlreadyPresent() {
        AtomicBoolean findByEmailWasCalled = new AtomicBoolean(false);
        AtomicBoolean findByUsernameWasCalled = new AtomicBoolean(false);

        var expected = new ErrorCodesException(ErrorCodes.USER_ALREADY_EXISTS);

        Mockito.when(repository.findUserEntityByEmail(Mockito.anyString())).then(invocation -> {
            findByEmailWasCalled.set(true);
            return Optional.empty();
        });

        Mockito.when(repository.findUserEntityByUsername(Mockito.anyString())).then(invocation -> {
            findByUsernameWasCalled.set(true);
            return foundUser;
        });

        ErrorCodesException actual = Assertions.assertThrowsExactly(ErrorCodesException.class, () -> {
            service.insertUser(foundUser.orElseThrow());
        });

        Assertions.assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
        Assertions.assertTrue(findByEmailWasCalled.get());
        Assertions.assertTrue(findByUsernameWasCalled.get());
    }

    @Test
    public void deleteUserByUsernameCompletesSuccessIfUserExists() {
        AtomicBoolean deleteInRepoWasCalled = new AtomicBoolean(false);

        Mockito.doAnswer(invocation -> {
            deleteInRepoWasCalled.set(true);
            return null;
        }).when(repository).deleteUserEntityByUsername(Mockito.anyString());

        service.deleteUser(Mockito.anyString());

        Assertions.assertTrue(deleteInRepoWasCalled.get());
    }

    @Test
    public void deleteUserThrowsErrorCodesExceptionWhenUserNotFound() {
        AtomicBoolean deleteInRepoWasCalled = new AtomicBoolean(false);

        var expected = new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);

        Mockito.when(repository.findUserEntityByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        Mockito.doAnswer(invocation -> {
            deleteInRepoWasCalled.set(true);
            return null;
        }).when(repository).deleteUserEntityByUsername(Mockito.anyString());

        ErrorCodesException actual = Assertions.assertThrowsExactly(ErrorCodesException.class, () -> {
            service.deleteUser(Mockito.anyString());
        });

        Assertions.assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
        Assertions.assertFalse(deleteInRepoWasCalled.get());
    }

    private List<UserEntity> generateUserEntities(Integer amount) {
        List<UserEntity> result = new ArrayList<>();
        for (var i = 1; i <= amount; ++i) {
            UserEntity entity = UserEntity.builder()
                    .uuid(UUID.randomUUID())
                    .email("email%1$d@host%1$d.com%1$d".formatted(i))
                    .username("username_" + i)
                    .password("password_" + i)
                    .avatar("avatar_" + i)
                    .role("user")
                    .build();

            result.add(entity);
        }
        return result;
    }
}