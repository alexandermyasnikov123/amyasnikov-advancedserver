package net.dunice.newsapi.dtos.requests;

import net.dunice.newsapi.validations.ValidAvatar;
import net.dunice.newsapi.validations.ValidEmail;
import net.dunice.newsapi.validations.ValidRegisterPassword;
import net.dunice.newsapi.validations.ValidRole;
import net.dunice.newsapi.validations.ValidUsername;

public record RegisterRequest(
        @ValidRegisterPassword
        String password,
        @ValidRole
        String role,
        @ValidUsername
        String name,
        @ValidEmail
        String email,
        @ValidAvatar
        String avatar
) {
}
