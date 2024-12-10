package net.dunice.features.users.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import net.dunice.features.core.dtos.constants.ValidationMessages;
import net.dunice.features.users.constants.UserValidationConstraints;
import net.dunice.features.users.validation.ValidEmail;

@Builder
public record UserRequest(
        @NotBlank(message = ValidationMessages.USER_AVATAR_NOT_NULL)
        @Size(
                min = UserValidationConstraints.MIN_AVATAR_LENGTH,
                max = UserValidationConstraints.MAX_AVATAR_LENGTH,
                message = ValidationMessages.USER_AVATAR_NOT_VALID
        )
        String avatar,
        @NotBlank(message = ValidationMessages.USER_NAME_HAS_TO_BE_PRESENT)
        @Size(
                min = UserValidationConstraints.MIN_USERNAME_LENGTH,
                max = UserValidationConstraints.MAX_USERNAME_LENGTH,
                message = ValidationMessages.USERNAME_SIZE_NOT_VALID
        )
        String name,
        @ValidEmail
        String email,
        @NotBlank(message = ValidationMessages.USER_ROLE_NOT_NULL)
        @Size(
                min = UserValidationConstraints.MIN_ROLE_LENGTH,
                max = UserValidationConstraints.MAX_ROLE_LENGTH,
                message = ValidationMessages.ROLE_SIZE_NOT_VALID
        )
        String role
) {
}
