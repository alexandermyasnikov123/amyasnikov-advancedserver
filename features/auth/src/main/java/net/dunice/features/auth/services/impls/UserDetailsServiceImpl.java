package net.dunice.features.auth.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.features.auth.repositories.UserDetailsRepository;
import net.dunice.features.auth.services.UserEntityDetailsService;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserEntityDetailsService {
    private final UserDetailsRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByUsername(username)
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public void deleteUserByUsername(String username) {
        repository.deleteByUsername(username);
    }
}
