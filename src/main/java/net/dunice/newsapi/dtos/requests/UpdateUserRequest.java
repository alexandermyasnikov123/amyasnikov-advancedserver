package net.dunice.newsapi.dtos.requests;

import jakarta.annotation.Nullable;
import net.dunice.newsapi.validations.ValidEmail;
import net.dunice.newsapi.validations.ValidRole;
import net.dunice.newsapi.validations.ValidUsername;

public record UpdateUserRequest(
        @Nullable
        String avatar,
        @ValidEmail
        String email,
        @ValidUsername
        String name,
        @ValidRole
        String role
) {
}
