package net.dunice.features.auth.dtos.responses;

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
