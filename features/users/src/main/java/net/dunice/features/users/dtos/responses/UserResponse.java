package net.dunice.features.users.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.With;
import java.util.UUID;

@With
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record UserResponse(
        String avatar,
        String email,
        UUID id,
        String name,
        String role
) {
}
