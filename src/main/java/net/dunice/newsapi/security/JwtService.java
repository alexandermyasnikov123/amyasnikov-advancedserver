package net.dunice.newsapi.security;

import java.util.UUID;

public interface JwtService {
    String extractUsername(String token);

    Boolean isTokenValid(String token, String username, String role, UUID uuid);

    String generateToken(String username, String role, UUID uuid);
}
