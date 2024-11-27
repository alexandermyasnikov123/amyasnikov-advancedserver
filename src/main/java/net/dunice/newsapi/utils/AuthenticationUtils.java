package net.dunice.newsapi.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.entities.UserEntityDetails;
import org.springframework.security.core.Authentication;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationUtils {
    public static UserEntity getUser(Authentication authentication) {
        return ((UserEntityDetails) authentication.getPrincipal()).entity();
    }
}
