package net.dunice.newsapi.dtos.requests;

import jakarta.validation.constraints.NotNull;
import net.dunice.newsapi.constants.ValidationMessages;
import net.dunice.newsapi.validations.ValidEmail;

public record LoginRequest(
        @ValidEmail
        String email,
        @NotNull(message = ValidationMessages.PASSWORD_NOT_VALID)
        String password
) {
}
