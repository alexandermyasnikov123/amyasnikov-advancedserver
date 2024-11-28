package net.dunice.newsapi.dtos.responses;

import lombok.With;
import java.util.UUID;

@With
public record AuthUserResponse(
        String avatar,
        String email,
        UUID id,
        String name,
        String role,
        String token
) {
}
