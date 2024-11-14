package net.dunice.newsapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.validations.ValidEmail;
import net.dunice.newsapi.validations.ValidPassword;
import net.dunice.newsapi.validations.ValidRole;
import net.dunice.newsapi.validations.ValidUsername;
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
public class UserEntity implements UserDetails {
    public static final int MIN_AVATAR_LENGTH = 3;

    public static final int MAX_AVATAR_LENGTH = 130;

    @Id
    @GeneratedValue
    UUID uuid;

    @Column(unique = true)
    @ValidUsername
    String username;

    @Column(unique = true)
    @ValidEmail
    String email;

    @ValidPassword
    String password;

    String avatar;

    @ValidRole
    String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }
}
