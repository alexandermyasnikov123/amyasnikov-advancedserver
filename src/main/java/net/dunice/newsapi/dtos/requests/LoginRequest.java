package net.dunice.newsapi.dtos.requests;

import net.dunice.newsapi.validations.ValidEmail;
import net.dunice.newsapi.validations.ValidLoginPassword;

public record LoginRequest(
        @ValidEmail
        String email,
        @ValidLoginPassword
        String password
) {
}
