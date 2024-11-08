package net.dunice.newsapi.services;

import net.dunice.newsapi.dtos.responses.UserResponse;
import net.dunice.newsapi.errors.UserEmailNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserEntityService {
    UserResponse loadByUsername(String username) throws UsernameNotFoundException;

    UserResponse loadByEmail(String email) throws UserEmailNotFoundException;
}
