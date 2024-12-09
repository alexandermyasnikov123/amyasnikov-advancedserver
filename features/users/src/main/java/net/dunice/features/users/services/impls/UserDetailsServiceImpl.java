package net.dunice.features.users.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.shared.entities.UserEntity;
import net.dunice.features.users.entities.UserEntityDetails;
import net.dunice.features.users.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = repository
                .findUserEntityByUsername(username)
                .orElseThrow(() -> new ErrorCodesException(ErrorCodes.USER_NOT_FOUND));

        return new UserEntityDetails(entity);
    }
}
