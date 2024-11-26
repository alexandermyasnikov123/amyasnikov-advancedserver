package net.dunice.newsapi.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.entities.callbacks.ImageDeleteCallbacks;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(value = ImageDeleteCallbacks.class)
public class UserEntity implements UserDetails, ImageProvider {
    @Id
    @GeneratedValue
    UUID uuid;

    @Column(unique = true)
    String username;

    @Column(unique = true)
    String email;

    String password;

    String avatar;

    String role;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_entity_id", referencedColumnName = "uuid")
    List<NewsEntity> news;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    @Transient
    public String getImageUrl() {
        return getAvatar();
    }
}
