package net.dunice.newsapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.validations.ValidPassword;
import net.dunice.newsapi.validations.ValidUsername;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue
    UUID uuid;

    @Column(unique = true)
    @ValidUsername
    String username;

    @ValidPassword
    String password;

    String avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name")
    RoleEntity role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
}

