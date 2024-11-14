package net.dunice.newsapi.dtos.responses;

import lombok.With;

@With
public record PublicUserResponse(
        String avatar,
        String email,
        String id,
        String name,
        String role
) {
}
