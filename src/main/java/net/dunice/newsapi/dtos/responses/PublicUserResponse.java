package net.dunice.newsapi.dtos.responses;

import lombok.With;
import java.util.UUID;

@With
public record PublicUserResponse(
        String avatar,
        String email,
        UUID id,
        String name,
        String role
) {
}
