package net.dunice.newsapi.services;

import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface UserService {
    List<PublicUserResponse> loadAllUsers();

    PublicUserResponse loadUserByUuid(String uuid);

    PublicUserResponse loadCurrentUser(Authentication authentication);

    PublicUserResponse loadByEmail(String email);

    PublicUserResponse updateUser(Authentication authentication, UpdateUserRequest request);

    PublicUserResponse insertUser(UserEntity entity);

    void deleteUser(Authentication authentication);
}
