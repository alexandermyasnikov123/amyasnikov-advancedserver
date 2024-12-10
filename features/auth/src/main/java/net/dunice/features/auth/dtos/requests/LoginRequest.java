package net.dunice.features.auth.dtos.requests;

import jakarta.validation.constraints.NotNull;
import net.dunice.features.auth.validation.ValidEmail;
import net.dunice.features.core.dtos.constants.ValidationMessages;

public record LoginRequest(
        @ValidEmail
        String email,
        @NotNull(message = ValidationMessages.PASSWORD_NOT_VALID)
        String password
) {
}
