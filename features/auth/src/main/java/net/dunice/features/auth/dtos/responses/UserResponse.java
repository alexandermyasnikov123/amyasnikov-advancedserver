package net.dunice.features.auth.dtos.responses;

import lombok.With;
import org.gradle.internal.impldep.com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@With
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record UserResponse(
        String avatar,
        String email,
        UUID id,
        String name,
        String role,
        String token
) {
}
