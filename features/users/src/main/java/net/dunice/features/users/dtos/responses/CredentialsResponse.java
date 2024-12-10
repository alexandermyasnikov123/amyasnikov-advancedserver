package net.dunice.features.users.dtos.responses;

import lombok.With;
import java.util.UUID;

@With
public record CredentialsResponse(
        UUID id,
        String username,
        String password,
        String role
) {
}
