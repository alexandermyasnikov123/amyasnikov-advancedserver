package net.dunice.features.users.services;

import net.dunice.features.users.dtos.requests.UserRequest;
import net.dunice.features.users.dtos.responses.UserResponse;
import net.dunice.features.users.validation.ValidEmail;
import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> loadAllUsers();

    UserResponse loadCurrent(HttpHeaders headers);

    UserResponse loadUserByUuid(UUID uuid);

    UserResponse loadUserByEmail(@ValidEmail String email);

    UserResponse updateUser(HttpHeaders headers, UserRequest request);

    UserResponse insertUser(UserRequest request);

    void deleteUser(HttpHeaders headers);

    void deleteUserByUsername(String username);
}
