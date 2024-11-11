package net.dunice.newsapi.errors;

import org.springframework.security.core.AuthenticationException;

public class UserEmailNotFoundException extends AuthenticationException {

    public UserEmailNotFoundException(String message) {
        super(message);
    }
}
