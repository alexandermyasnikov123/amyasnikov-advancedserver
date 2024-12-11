package net.dunice.features.auth.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.features.auth.constants.AuthConstants;
import net.dunice.features.auth.entities.UserEntityDetails;
import net.dunice.features.auth.repositories.UserDetailsRepository;
import net.dunice.features.auth.services.UserEntityDetailsService;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserEntityDetailsService {
    private final UserDetailsRepository repository;

    @Override
    public UserEntityDetails createDetails(UUID id, String username, String passwordHash) {
        return repository.save(
                UserEntityDetails.builder()
                        .uuid(id)
                        .username(username)
                        .password(passwordHash)
                        .role(AuthConstants.ONLY_ROLE)
                        .build()
        );
    }

    @Override
    public UserEntityDetails loadUserById(UUID id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public UserEntityDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByUsername(username)
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public void deleteUserByUsername(String username) {
        UserDetails details = loadUserByUsername(username);
        repository.deleteByUsername(details.getUsername());
    }
}
