package net.dunice.newsapi.services.impls;

import net.dunice.newsapi.BaseTestCase;
import net.dunice.features.news.mappers.UserEntityMapper;
import net.dunice.newsapi.security.JwtService;
import net.dunice.newsapi.services.UserService;
import net.dunice.newsapi.services.defaults.UserTestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

public class AuthServiceImplTest extends BaseTestCase {
    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserEntityMapper mapper;

    @Mock
    private UserService userService;

    private AuthServiceImpl authService;

    @BeforeEach
    public void beforeEach() {
        authService = new AuthServiceImpl(jwtService, encoder, authenticationManager, mapper, userService);
    }

    @Test
    public void registerUser_InsertsUserAndGeneratesBearerTokenWithHeader() {
        when(mapper.entityToPublicResponse(UserTestConstants.ENCODED_USER_ENTITY))
                .thenReturn(UserTestConstants.COMMON_PUBLIC_RESPONSE);

        when(mapper.publicResponseToAuth(
                UserTestConstants.COMMON_PUBLIC_RESPONSE,
                UserTestConstants.JWT_TOKEN
        )).thenReturn(UserTestConstants.COMMON_AUTH_RESPONSE);

        when(userService.insertUser(UserTestConstants.ENCODED_USER_ENTITY))
                .thenReturn(UserTestConstants.COMMON_PUBLIC_RESPONSE);

        when(mapper.registerRequestToEntity(UserTestConstants.ENCODED_REGISTER_REQUEST))
                .thenReturn(UserTestConstants.ENCODED_USER_ENTITY);

        when(encoder.encode(UserTestConstants.SIMPLE_PASSWORD))
                .thenReturn(UserTestConstants.ENCODED_PASSWORD);

        when(jwtService.generateTokenWithHeader(
                UserTestConstants.ENCODED_USER_ENTITY.getUsername(),
                UserTestConstants.ENCODED_USER_ENTITY.getRole(),
                UserTestConstants.ENCODED_USER_ENTITY.getId()
        )).thenReturn(UserTestConstants.JWT_TOKEN);

        authService.registerUser(UserTestConstants.VALID_REGISTER_REQUEST);

        InOrder inOrder = inOrder(encoder, userService, jwtService);

        inOrder.verify(encoder).encode(UserTestConstants.SIMPLE_PASSWORD);
        inOrder.verify(userService).insertUser(UserTestConstants.ENCODED_USER_ENTITY);
        inOrder.verify(jwtService).generateTokenWithHeader(
                UserTestConstants.ENCODED_USER_ENTITY.getUsername(),
                UserTestConstants.ENCODED_USER_ENTITY.getRole(),
                UserTestConstants.ENCODED_USER_ENTITY.getId()
        );
    }

    @Test
    public void loginUser_AuthenticatesUser() {
        when(userService.loadByEmail(anyString()))
                .thenReturn(UserTestConstants.COMMON_PUBLIC_RESPONSE);

        when(jwtService.generateTokenWithHeader(anyString(), anyString(), any()))
                .thenReturn(UserTestConstants.JWT_TOKEN);

        when(authenticationManager.authenticate(any()))
                .thenReturn(null);

        authService.loginUser(UserTestConstants.VALID_LOGIN_REQUEST);

        InOrder inOrder = inOrder(userService, jwtService, authenticationManager);

        inOrder.verify(userService).loadByEmail(anyString());
        inOrder.verify(jwtService).generateTokenWithHeader(anyString(), anyString(), any());
        inOrder.verify(authenticationManager).authenticate(any());
    }
}
