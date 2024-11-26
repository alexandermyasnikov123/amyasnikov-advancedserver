package net.dunice.newsapi.services.impls;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.dtos.requests.LoginRequest;
import net.dunice.newsapi.dtos.requests.RegisterRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.security.JwtService;
import net.dunice.newsapi.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImplTest {
    JwtService jwtService = Mockito.mock(JwtService.class);

    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

    AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);

    UserEntityMapper mapper = Mockito.mock(UserEntityMapper.class);

    UserService userService = Mockito.mock(UserService.class);

    AuthServiceImpl authService = new AuthServiceImpl(jwtService, encoder, authenticationManager, mapper, userService);

    PublicUserResponse fakePublicResponse = new PublicUserResponse(
            "avatar", "email", UUID.randomUUID().toString(), "name", "role"
    );

    AuthUserResponse fakeAuthResponse = new AuthUserResponse(
            "avatar", "email", fakePublicResponse.id(), "name", "role", "token"
    );

    @BeforeEach
    public void beforeEach() {
        Mockito.when(mapper.entityToPublicResponse(Mockito.any())).thenReturn(fakePublicResponse);
        Mockito.when(mapper.publicResponseToAuth(Mockito.any(), Mockito.anyString())).thenReturn(fakeAuthResponse);
    }

    @Test
    public void registerUser_InsertsUserAndGeneratesBearerTokenWithHeader() {
        AtomicBoolean insertUserWasCalled = new AtomicBoolean(false);
        AtomicBoolean generateTokenWasCalled = new AtomicBoolean(false);

        Mockito.when(userService.insertUser(Mockito.any())).then(invocation -> {
            insertUserWasCalled.set(true);
            return fakePublicResponse;
        });

        Mockito.when(jwtService.generateTokenWithHeader(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .then(invocation -> {
                    generateTokenWasCalled.set(true);
                    return "any token";
                });

        RegisterRequest mockRegisterRequest = Mockito.mock(RegisterRequest.class);

        AuthUserResponse actual = authService.registerUser(mockRegisterRequest);
        Assertions.assertEquals(fakeAuthResponse, actual);
        Assertions.assertTrue(insertUserWasCalled.get());
        Assertions.assertTrue(generateTokenWasCalled.get());
    }

    @Test
    public void loginUser_AuthenticatesUser() {
        AtomicBoolean loadByEmailWasCalled = new AtomicBoolean(false);
        AtomicBoolean generateTokenWasCalled = new AtomicBoolean(false);
        AtomicBoolean authenticateWasCalled = new AtomicBoolean(false);

        LoginRequest loginRequest = new LoginRequest("email", "password");

        Mockito.when(userService.loadByEmail(Mockito.anyString())).then(invocation -> {
            loadByEmailWasCalled.set(true);
            return fakePublicResponse;
        });

        Mockito.when(jwtService.generateTokenWithHeader(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .then(invocation -> {
                    generateTokenWasCalled.set(true);
                    return "any token";
                });

        Mockito.when(authenticationManager.authenticate(Mockito.any())).then(invocation -> {
            authenticateWasCalled.set(true);

            UsernamePasswordAuthenticationToken token = invocation.getArgument(0);
            Assertions.assertNotNull(token);

            return null;
        });

        authService.loginUser(loginRequest);

        Assertions.assertTrue(authenticateWasCalled.get());
        Assertions.assertTrue(loadByEmailWasCalled.get());
        Assertions.assertTrue(generateTokenWasCalled.get());
    }

    @Test
    public void createAuthenticationTokenIfValid_ReturnsNonEmptyTokenIfValidCredentials() {
        AtomicBoolean extractUsernameCalled = new AtomicBoolean(false);
        AtomicBoolean loadUserByUsernameCalled = new AtomicBoolean(false);
        AtomicBoolean isTokenValidCalled = new AtomicBoolean(false);

        Mockito.when(jwtService.extractUsername(Mockito.anyString())).then(invocation -> {
            extractUsernameCalled.set(true);
            return "username";
        });

        Mockito.when(userService.loadUserByUsername(Mockito.anyString())).then(invocation -> {
            loadUserByUsernameCalled.set(true);
            return UserEntity.builder().role("role").build();
        });

        Mockito.when(jwtService.isTokenValid(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).then(invocation -> {
            isTokenValidCalled.set(true);
            return true;
        });

        Optional<AbstractAuthenticationToken> actual = authService.createAuthenticationTokenIfValid("any token");
        Assertions.assertTrue(extractUsernameCalled.get());
        Assertions.assertTrue(loadUserByUsernameCalled.get());
        Assertions.assertTrue(isTokenValidCalled.get());
        Assertions.assertTrue(actual.isPresent());
    }

    @Test
    public void createAuthenticationTokenIfValid_ReturnsEmptyTokenIfNotValidCredentials() {
        AtomicBoolean extractUsernameCalled = new AtomicBoolean(false);
        AtomicBoolean loadUserByUsernameCalled = new AtomicBoolean(false);
        AtomicBoolean isTokenValidCalled = new AtomicBoolean(false);

        Mockito.when(jwtService.extractUsername(Mockito.anyString())).then(invocation -> {
            extractUsernameCalled.set(true);
            return "username";
        });

        Mockito.when(userService.loadUserByUsername(Mockito.anyString())).then(invocation -> {
            loadUserByUsernameCalled.set(true);
            return UserEntity.builder().role("role").build();
        });

        Mockito.when(jwtService.isTokenValid(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).then(invocation -> {
            isTokenValidCalled.set(true);
            return false;
        });

        Optional<AbstractAuthenticationToken> actual = authService.createAuthenticationTokenIfValid("any token");
        Assertions.assertTrue(extractUsernameCalled.get());
        Assertions.assertTrue(loadUserByUsernameCalled.get());
        Assertions.assertTrue(isTokenValidCalled.get());
        Assertions.assertTrue(actual.isEmpty());
    }
}