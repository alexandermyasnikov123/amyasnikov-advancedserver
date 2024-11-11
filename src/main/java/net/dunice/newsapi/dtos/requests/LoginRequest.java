package net.dunice.newsapi.dtos.requests;

import net.dunice.newsapi.validations.ValidEmail;
import net.dunice.newsapi.validations.ValidPassword;

public record LoginRequest(
        @ValidEmail
        String email,
        @ValidPassword
        String password
) {
}
