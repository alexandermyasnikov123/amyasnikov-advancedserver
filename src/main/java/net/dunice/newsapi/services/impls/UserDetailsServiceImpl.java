package net.dunice.newsapi.services.impls;

import lombok.RequiredArgsConstructor;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.entities.UserEntityDetails;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.repositories.UsersRepository;
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
