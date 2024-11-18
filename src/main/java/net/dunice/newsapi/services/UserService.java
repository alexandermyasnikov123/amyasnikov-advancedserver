package net.dunice.newsapi.services;

import jakarta.transaction.Transactional;
import net.dunice.newsapi.dtos.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<PublicUserResponse> loadAllUsers();

    PublicUserResponse loadUserByUuid(String uuid);

    PublicUserResponse loadByEmail(String email);

    PublicUserResponse updateUser(String uuid, UpdateUserRequest request);

    Boolean hasUserWithUsername(String username);

    Boolean hasUserWithEmail(String email);

    @Override
    UserEntity loadUserByUsername(String username);

    @Transactional
    PublicUserResponse insertUser(UserEntity entity);

    void deleteUserByUsername(String username);
}
