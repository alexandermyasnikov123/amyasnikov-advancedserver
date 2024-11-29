package net.dunice.newsapi.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.entities.UserEntityDetails;
import net.dunice.newsapi.errors.ErrorCodesException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationUtils {
    public static UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserEntityDetails(UserEntity entity)) {
            return entity;
        }

        throw new ErrorCodesException(ErrorCodes.UNAUTHORISED);
    }
}
