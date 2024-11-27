package net.dunice.newsapi.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;
import net.dunice.newsapi.entities.callbacks.ImageDeleteCallbacks;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(value = ImageDeleteCallbacks.class)
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

    private String password;

    private String avatar;

    private String role;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_entity_id", referencedColumnName = "uuid")
    @EqualsAndHashCode.Exclude
    private List<NewsEntity> news;
}
