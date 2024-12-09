package net.dunice.features.auth.dtos.requests;

import jakarta.validation.constraints.NotNull;
import net.dunice.features.core.dtos.constants.ValidationMessages;
import net.dunice.features.users.validation.ValidEmail;

public record LoginRequest(
        @ValidEmail
        String email,
        @NotNull(message = ValidationMessages.PASSWORD_NOT_VALID)
        String password
) {
}
