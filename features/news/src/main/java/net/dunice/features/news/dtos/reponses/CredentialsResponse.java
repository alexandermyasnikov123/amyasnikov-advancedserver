package net.dunice.features.news.dtos.reponses;

import lombok.With;
import java.util.UUID;

@With
public record CredentialsResponse(
        UUID uuid,
        String username,
        String password,
        String role
) {
}
