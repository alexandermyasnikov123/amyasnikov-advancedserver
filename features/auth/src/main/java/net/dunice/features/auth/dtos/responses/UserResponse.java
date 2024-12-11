package net.dunice.features.auth.dtos.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;
import org.gradle.internal.impldep.com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@With
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record UserResponse(
        @JsonProperty String avatar,
        @JsonProperty String email,
        @JsonProperty UUID id,
        @JsonProperty String name,
        @JsonProperty String role,
        @JsonProperty String token
) {
    @JsonCreator
    public UserResponse {
    }
}
