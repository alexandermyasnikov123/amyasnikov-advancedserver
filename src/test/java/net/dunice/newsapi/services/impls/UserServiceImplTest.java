package net.dunice.newsapi.services.impls;

import net.dunice.newsapi.BaseTestCase;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.repositories.UsersRepository;
import net.dunice.newsapi.services.defaults.UserTestConstants;
import net.dunice.newsapi.services.defaults.UserTestDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest extends BaseTestCase {
    private final List<UserEntity> allUsers = UserTestDefaults.generateUserEntities(10);

    private final List<PublicUserResponse> allPublicUsers = allUsers.stream()
            .map(entity -> new PublicUserResponse(
                    entity.getAvatar(),
                    entity.getEmail(),
                    entity.getId(),
                    entity.getUsername(),
                    entity.getRole()
            )).toList();

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
        when(repository.findById(any()))
                .thenReturn(Optional.of(UserTestConstants.COMMON_USER_ENTITY));

        when(mapper.entityToPublicResponse(UserTestConstants.COMMON_USER_ENTITY))
                .thenReturn(UserTestConstants.COMMON_PUBLIC_RESPONSE);

        PublicUserResponse actual = service.loadUserByUuid(UUID.randomUUID().toString());

        Assertions.assertEquals(UserTestConstants.COMMON_PUBLIC_RESPONSE, actual);
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
        when(repository.findUserEntityByEmail(anyString()))
                .thenReturn(Optional.of(UserTestConstants.COMMON_USER_ENTITY));

        when(mapper.entityToPublicResponse(UserTestConstants.COMMON_USER_ENTITY))
                .thenReturn(UserTestConstants.COMMON_PUBLIC_RESPONSE);

        PublicUserResponse actual = service.loadByEmail(UserTestConstants.SIMPLE_EMAIL);

        Assertions.assertEquals(UserTestConstants.COMMON_PUBLIC_RESPONSE, actual);
    }

    @Test
    public void loadNotExistingUserByEmailThrowsErrorCodesException() {
        when(repository.findUserEntityByEmail(any())).thenReturn(Optional.empty());

        ErrorCodesException expected = new ErrorCodesException(ErrorCodes.USER_NOT_FOUND);

        ErrorCodesException actual = assertThrowsExactly(ErrorCodesException.class, () -> {
            service.loadByEmail(UserTestConstants.SIMPLE_EMAIL);
        });

        assertEquals(expected.getErrorCodes(), actual.getErrorCodes());
    }

}
