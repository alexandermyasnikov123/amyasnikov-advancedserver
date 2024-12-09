package net.dunice.newsapi.services.defaults;

import net.dunice.features.news.requests.LoginRequest;
import net.dunice.features.news.requests.RegisterRequest;
import net.dunice.features.news.requests.UpdateUserRequest;
import net.dunice.newsapi.dtos.responses.AuthUserResponse;
import net.dunice.newsapi.dtos.responses.PublicUserResponse;
import net.dunice.newsapi.entities.UserEntity;
import org.springframework.security.core.token.Sha512DigestUtils;
import java.util.List;
import java.util.UUID;

public interface UserTestConstants {
    String ENCODED_PASSWORD = "eNcOdEd_PaSsWoRd";

    String SIMPLE_PASSWORD = "password1234";

    String SIMPLE_EMAIL = "simple.email@address.net";

    String JWT_TOKEN = Sha512DigestUtils.shaHex("some secret information");

    UserEntity COMMON_USER_ENTITY = new UserEntity(
            UUID.randomUUID(),
            "username_1",
            SIMPLE_EMAIL,
            SIMPLE_PASSWORD,
            "http://www.my-domain.com/image.jpg",
            "user",
            List.of()
    );

    UserEntity ENCODED_USER_ENTITY = COMMON_USER_ENTITY.withPassword(ENCODED_PASSWORD);

    PublicUserResponse COMMON_PUBLIC_RESPONSE = new PublicUserResponse(
            COMMON_USER_ENTITY.getAvatar(),
            COMMON_USER_ENTITY.getEmail(),
            COMMON_USER_ENTITY.getId(),
            COMMON_USER_ENTITY.getUsername(),
            COMMON_USER_ENTITY.getRole()
    );


    AuthUserResponse COMMON_AUTH_RESPONSE = new AuthUserResponse(
            COMMON_PUBLIC_RESPONSE.avatar(),
            COMMON_PUBLIC_RESPONSE.email(),
            COMMON_PUBLIC_RESPONSE.id(),
            COMMON_PUBLIC_RESPONSE.name(),
            COMMON_PUBLIC_RESPONSE.role(),
            JWT_TOKEN
    );

    RegisterRequest VALID_REGISTER_REQUEST = new RegisterRequest(
            SIMPLE_PASSWORD,
            COMMON_PUBLIC_RESPONSE.role(),
            COMMON_PUBLIC_RESPONSE.name(),
            COMMON_PUBLIC_RESPONSE.email(),
            COMMON_PUBLIC_RESPONSE.avatar()
    );

    RegisterRequest ENCODED_REGISTER_REQUEST = VALID_REGISTER_REQUEST.withPassword(ENCODED_PASSWORD);

    LoginRequest VALID_LOGIN_REQUEST = new LoginRequest(
            VALID_REGISTER_REQUEST.email(),
            VALID_REGISTER_REQUEST.password()
    );

    UpdateUserRequest UPDATE_USER_REQUEST = new UpdateUserRequest(
            "http://new-domain.net/new-image.jpg",
            "new_username_1",
            "new.email@domain.net",
            "new_role"
    );
}
