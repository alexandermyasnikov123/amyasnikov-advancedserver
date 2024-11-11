package net.dunice.newsapi.security;

import java.util.Map;

public interface JwtService {
    String extractUsername(String token);

    String generateToken(String username);

    String generateToken(String username, Map<String, Object> extraClaims);
}
