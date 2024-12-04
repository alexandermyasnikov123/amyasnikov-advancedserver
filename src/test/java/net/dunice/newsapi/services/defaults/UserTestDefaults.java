package net.dunice.newsapi.services.defaults;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dunice.newsapi.entities.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserTestDefaults {

    public static List<UserEntity> generateUserEntities(Integer amount) {
        List<UserEntity> result = new ArrayList<>();
        for (var i = 1; i <= amount; ++i) {
            UserEntity entity = UserEntity.builder()
                    .id(UUID.randomUUID())
                    .email("email%1$d@host%1$d.com%1$d".formatted(i))
                    .username("username_" + i)
                    .password("password_" + i)
                    .avatar("avatar_" + i)
                    .role("user")
                    .build();

            result.add(entity);
        }
        return result;
    }
}
