package net.dunice.features.users.dtos.responses;

import lombok.With;
import java.util.UUID;

@With
public record UserResponse(
        String avatar,
        String email,
        UUID id,
        String name,
        String role
) {
}
