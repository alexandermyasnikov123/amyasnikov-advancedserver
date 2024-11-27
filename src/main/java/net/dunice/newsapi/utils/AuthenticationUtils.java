package net.dunice.newsapi.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.entities.UserEntityDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationUtils {
    public static UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserEntityDetails) authentication.getPrincipal()).entity();
    }
}
