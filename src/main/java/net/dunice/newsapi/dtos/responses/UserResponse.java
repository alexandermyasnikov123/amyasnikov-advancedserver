package net.dunice.newsapi.dtos.responses;

public record UserResponse(
        String avatar,
        String email,
        String id,
        String name,
        String role,
        String token
) {
}
