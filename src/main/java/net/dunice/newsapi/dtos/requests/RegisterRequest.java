package net.dunice.newsapi.dtos.requests;

import jakarta.annotation.Nullable;
import net.dunice.newsapi.validations.ValidEmail;
import net.dunice.newsapi.validations.ValidPassword;
import net.dunice.newsapi.validations.ValidRole;
import net.dunice.newsapi.validations.ValidUsername;

public record RegisterRequest(
        @ValidPassword
        String password,
        @ValidRole
        String role,
        @ValidUsername
        String name,
        @ValidEmail
        String email,
        @Nullable
        String avatar
) {
}
