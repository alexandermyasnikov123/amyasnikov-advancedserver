package net.dunice.features.auth.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.features.auth.clients.UserApiClient;
import net.dunice.features.auth.dtos.requests.LoginRequest;
import net.dunice.features.auth.dtos.requests.RegisterRequest;
import net.dunice.features.auth.dtos.responses.UserResponse;
import net.dunice.features.auth.entities.UserEntityDetails;
import net.dunice.features.auth.kafka.UserEventProducer;
import net.dunice.features.auth.security.JwtService;
import net.dunice.features.auth.services.AuthService;
import net.dunice.features.auth.services.UserEntityDetailsService;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.core.dtos.utils.ResponseUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final UserApiClient userApiClient;

    private final UserEntityDetailsService userDetailsService;

    private final UserEventProducer userEventProducer;

    @Override
    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        RegisterRequest encodedRequest = request.withEncodedPassword(encoder);

        UserResponse response = ResponseUtils.tryExtractData(userApiClient.insertUser(encodedRequest));

        UserEntityDetails details = userDetailsService.createDetails(
                response.id(), response.name(), encodedRequest.password()
        );

        String token = jwtService.generateTokenWithHeader(
                response.name(),
                response.id()
        );

        return response
                .withToken(token)
                .withRole(details.getRole());
    }

    @Override
    public UserResponse loginUser(LoginRequest request) {
        UserResponse response = ResponseUtils.tryExtractData(
                userApiClient.loadByEmail(request.email())
        );

        UserEntityDetails details = userDetailsService.loadUserById(response.id());

        String token = jwtService.generateTokenWithHeader(
                response.name(),
                response.id()
        );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(response.name(), request.password())
        );

        return response
                .withToken(token)
                .withRole(details.getRole());
    }

    @Override
    public UserDetails loadCurrentAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails details) {
            return details;
        }

        throw new ErrorCodesException(ErrorCodes.UNAUTHORISED);
    }

    @Override
    @Transactional
    public void deleteCurrentAuth() {
        UserDetails current = loadCurrentAuth();
        final String username = current.getUsername();

        if (userDetailsService.deleteUserByUsername(username)) {
            userEventProducer.produceUserDeleted(username);
        }
    }
}
