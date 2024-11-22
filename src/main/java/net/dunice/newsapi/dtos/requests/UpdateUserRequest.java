package net.dunice.newsapi.dtos.requests;

import lombok.Builder;
import net.dunice.newsapi.validations.ValidAvatar;
import net.dunice.newsapi.validations.ValidEmail;
import net.dunice.newsapi.validations.ValidRole;
import net.dunice.newsapi.validations.ValidUsername;

@Builder
public record UpdateUserRequest(
        @ValidAvatar
        String avatar,
        @ValidUsername
        String name,
        @ValidEmail
        String email,
        @ValidRole
        String role
) {
}
