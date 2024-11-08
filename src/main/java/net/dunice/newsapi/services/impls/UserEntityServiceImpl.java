package net.dunice.newsapi.services.impls;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.dunice.newsapi.dtos.responses.UserResponse;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.UserEmailNotFoundException;
import net.dunice.newsapi.errors.ValidationConstants;
import net.dunice.newsapi.mappers.UserEntityMapper;
import net.dunice.newsapi.repositories.UsersRepository;
import net.dunice.newsapi.services.JwtService;
import net.dunice.newsapi.services.UserEntityService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEntityServiceImpl implements UserEntityService, UserDetailsService {
    UsersRepository repository;

    UserEntityMapper mapper;

    JwtService jwtService;

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findUserEntityByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(ValidationConstants.USER_NOT_FOUND)
        );
    }

    @Override
    public UserResponse loadByUsername(String username) throws UsernameNotFoundException {
        return mapper.entityToResponse(loadUserByUsername(username), jwtService.getCurrentToken());
    }

    @Override
    public UserResponse loadByEmail(String email) throws UserEmailNotFoundException {
        UserEntity entity = repository.findUserEntityByEmail(email).orElseThrow(() ->
                new UserEmailNotFoundException(ValidationConstants.USER_NOT_FOUND)
        );

        return mapper.entityToResponse(entity, jwtService.getCurrentToken());
    }
}
