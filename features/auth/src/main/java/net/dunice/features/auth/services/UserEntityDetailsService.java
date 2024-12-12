package net.dunice.features.auth.services;

import net.dunice.features.auth.entities.UserEntityDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.UUID;

public interface UserEntityDetailsService extends UserDetailsService {
    @Override
    UserEntityDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserEntityDetails loadUserById(UUID id);

    UserEntityDetails createDetails(UUID id, String username, String passwordHash);

    Boolean deleteUserByUsername(String username);
}
