package net.dunice.features.users.entities;

import lombok.experimental.Delegate;
import net.dunice.features.shared.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public record UserEntityDetails(@Delegate UserEntity entity) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(entity.getRole()));
    }
}
