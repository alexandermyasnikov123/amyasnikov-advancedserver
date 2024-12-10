package net.dunice.features.users.services;

import net.dunice.features.users.dtos.requests.UserRequest;
import net.dunice.features.users.dtos.responses.UserResponse;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> loadAllUsers();

    UserResponse loadCurrent();

    UserResponse loadUserByUuid(UUID uuid);

    UserResponse loadByEmail(String email);

    UserResponse updateUser(UserRequest request);

    UserResponse insertUser(UserRequest request);

    void deleteUser();
}
