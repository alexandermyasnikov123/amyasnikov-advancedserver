package net.dunice.newsapi.dtos.responses;

import lombok.With;

@With
public record UserResponse(
        String avatar,
        String email,
        String id,
        String name,
        String role,
        String token
) {
}
