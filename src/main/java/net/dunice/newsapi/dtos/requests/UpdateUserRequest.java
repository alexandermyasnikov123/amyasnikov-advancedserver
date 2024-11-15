package net.dunice.newsapi.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import net.dunice.newsapi.constants.ValidationConstants;
import net.dunice.newsapi.validations.ValidAvatar;
import net.dunice.newsapi.validations.ValidRole;
import net.dunice.newsapi.validations.ValidUsername;

public record UpdateUserRequest(
        @ValidAvatar
        String avatar,
        @ValidUsername
        String name,
        @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
        @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_NULL)
        String email,
        @ValidRole
        String role
) {
}
