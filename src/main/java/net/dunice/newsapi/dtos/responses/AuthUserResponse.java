package net.dunice.newsapi.dtos.responses;

import lombok.With;

@With
public record AuthUserResponse(
        String avatar,
        String email,
        String id,
        String name,
        String role,
        String token
) {
}
