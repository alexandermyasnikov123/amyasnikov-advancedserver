package net.dunice.features.users.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import java.util.UUID;

@Entity
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIncludeProperties(value = {"userId", "username"})
public class UserEntity {
    @Id
    @Column(name = "uuid")
    @GeneratedValue
    @JsonProperty(value = "userId", required = true)
    private UUID id;

    @Column(unique = true)
    @JsonProperty(value = "username", required = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String avatar;
}
