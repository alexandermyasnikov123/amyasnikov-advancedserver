package net.dunice.features.users.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.shared.entities.UserEntity;
import net.dunice.features.users.entities.UserEntityDetails;
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
