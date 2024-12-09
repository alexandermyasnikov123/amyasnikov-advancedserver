package net.dunice.features.users.services;

import net.dunice.features.shared.entities.UserEntity;
import net.dunice.features.users.dtos.requests.UpdateUserRequest;
import net.dunice.features.users.dtos.responses.PublicUserResponse;
import java.util.List;

public interface UserService {
    List<PublicUserResponse> loadAllUsers();

    PublicUserResponse loadUserByUuid(String uuid);

    PublicUserResponse loadCurrentUser();

    PublicUserResponse loadByEmail(String email);

    PublicUserResponse updateUser(UpdateUserRequest request);

    PublicUserResponse insertUser(UserEntity entity);

    void deleteUser();
}
