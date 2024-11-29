package net.dunice.newsapi.services.impls;

import net.dunice.newsapi.BaseTestCase;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.repositories.UsersRepository;
import net.dunice.newsapi.services.defaults.UserTestConstants;
import net.dunice.newsapi.services.defaults.UserTestDefaults;
import net.dunice.newsapi.utils.AuthenticationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest extends BaseTestCase {
    private final List<UserEntity> allUsers = UserTestDefaults.generateUserEntities(10);

    @Mock
    private UsersRepository repository;

    @Mock
    private UserEntityMapper mapper;

    private UserServiceImpl service;

    @BeforeEach
    public void beforeEach() {
        service = new UserServiceImpl(repository, mapper);
    }

    @Test
    public void loadAllUsersReturnsPublicResponse() {
        when(repository.findAll()).thenReturn(allUsers);
        when(mapper.entityToPublicResponse(any())).thenReturn(UserTestConstants.COMMON_PUBLIC_RESPONSE);

        service.loadAllUsers();

        verify(repository).findAll();
    }

    @Test
    public void loadUserByUuidReturnsRequiredUserIfExists() {
        UserEntity requiredEntity = UserTestConstants.COMMON_USER_ENTITY;
        PublicUserResponse requiredResponse = UserTestConstants.COMMON_PUBLIC_RESPONSE;

        UUID requiredId = requiredEntity.getId();

        when(repository.findById(requiredId))
                .thenReturn(Optional.of(requiredEntity));

        when(mapper.entityToPublicResponse(requiredEntity))
                .thenReturn(requiredResponse);

        PublicUserResponse actual = service.loadUserByUuid(requiredId.toString());

        Assertions.assertEquals(requiredResponse, actual);
    }

    @Test
    public void loadNotExistingUserByUuidThrowsErrorCodesException() {
        when(repository.findById(any()))
                .thenReturn(Optional.empty());

        ErrorCodesException expected = new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);

        ErrorCodesException actual = assertThrowsExactly(ErrorCodesException.class, () -> {
            service.loadUserByUuid(UUID.randomUUID().toString());
        });

        assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
    }

    @Test
    public void loadUserByEmailReturnsRequiredUserIfExists() {
        UserEntity requiredEntity = UserTestConstants.COMMON_USER_ENTITY;
        PublicUserResponse requiredResponse = UserTestConstants.COMMON_PUBLIC_RESPONSE;

        when(repository.findUserEntityByEmail(requiredEntity.getEmail()))
                .thenReturn(Optional.of(requiredEntity));

        when(mapper.entityToPublicResponse(requiredEntity))
                .thenReturn(requiredResponse);

        PublicUserResponse actual = service.loadByEmail(requiredEntity.getEmail());

        Assertions.assertEquals(requiredResponse, actual);
    }

    @Test
    public void loadNotExistingUserByEmailThrowsErrorCodesException() {
        when(repository.findUserEntityByEmail(any()))
                .thenReturn(Optional.empty());

        ErrorCodesException expected = new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);

        ErrorCodesException actual = assertThrowsExactly(ErrorCodesException.class, () -> {
            service.loadByEmail(UserTestConstants.SIMPLE_EMAIL);
        });

        assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
    }

    @Test
    public void updateUserUpdatesUserAndMapResponse() {
        try (MockedStatic<AuthenticationUtils> utility = mockStatic(AuthenticationUtils.class)) {
            UpdateUserRequest updateRequest = UserTestConstants.UPDATE_USER_REQUEST;
            UserEntity currentUser = UserTestConstants.COMMON_USER_ENTITY;

            UserEntity updatedUser = UserTestConstants.ENCODED_USER_ENTITY;
            PublicUserResponse updatedResponse = UserTestConstants.COMMON_PUBLIC_RESPONSE;

            utility.when(AuthenticationUtils::getCurrentUser)
                    .thenReturn(currentUser);

            when(mapper.updateRequestToEntity(currentUser.getId(), currentUser.getPassword(), updateRequest))
                    .thenReturn(updatedUser);

            when(mapper.entityToPublicResponse(updatedUser))
                    .thenReturn(updatedResponse);

            PublicUserResponse actualResponse = service.updateUser(updateRequest);

            verify(repository).save(updatedUser);

            assertEquals(updatedResponse, actualResponse);
        }
    }

    @Test
    public void insertUserInsertsUserIfUsernameAndEmailAreUnique() {
        UserEntity initialUser = UserTestConstants.COMMON_USER_ENTITY;
        UserEntity expectedUser = UserTestConstants.ENCODED_USER_ENTITY;

        PublicUserResponse expectedResponse = UserTestConstants.COMMON_PUBLIC_RESPONSE;

        when(repository.findUserEntityByEmail(initialUser.getEmail()))
                .thenReturn(Optional.empty());

        when(repository.findUserEntityByUsername(initialUser.getUsername()))
                .thenReturn(Optional.empty());

        when(repository.save(initialUser))
                .thenReturn(expectedUser);

        when(mapper.entityToPublicResponse(expectedUser))
                .thenReturn(expectedResponse);

        PublicUserResponse actual = service.insertUser(initialUser);

        InOrder inOrder = inOrder(repository);

        inOrder.verify(repository).findUserEntityByEmail(initialUser.getEmail());
        inOrder.verify(repository).findUserEntityByUsername(initialUser.getUsername());

        assertEquals(expectedResponse, actual);
    }

    @Test
    public void insertUserThrowsErrorCodesExceptionIfEmailAlreadyPresent() {
        UserEntity foundUser = UserTestConstants.COMMON_USER_ENTITY;
        String foundEmail = foundUser.getEmail();

        ErrorCodesException expectedException = new ErrorCodesException(ErrorCodes.USER_ALREADY_EXISTS);

        when(repository.findUserEntityByEmail(foundEmail))
                .thenReturn(Optional.of(foundUser));

        when(repository.findUserEntityByUsername(anyString()))
                .thenReturn(Optional.empty());

        ErrorCodesException actual = assertThrowsExactly(ErrorCodesException.class, () -> {
            service.insertUser(foundUser);
        });

        Assertions.assertEquals(expectedException.getErrorCodes(), actual.getErrorCodes());

        verify(repository).findUserEntityByEmail(foundEmail);
        verify(repository, never()).findUserEntityByUsername(anyString());
    }
}
