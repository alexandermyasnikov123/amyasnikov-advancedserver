package net.dunice.newsapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.dunice.newsapi.constants.JwtDefaults;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;

@Service
public class JwtService {
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(decodeSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(JwtDefaults.USERNAME_CLAIM, String.class);
    }

    public Boolean isTokenValid(String token, String username, String role, UUID uuid) {
        Claims claims = extractClaims(token);

        Boolean isNotExpired = claims.getExpiration().after(new Date());
        Boolean validUsername = claims.get(JwtDefaults.USERNAME_CLAIM).equals(username);
        Boolean validRole = claims.get(JwtDefaults.ROLE_CLAIM).equals(role);
        Boolean validUuid = claims.get(JwtDefaults.ID_CLAIM).equals(uuid.toString());

        return isNotExpired && validUsername && validRole && validUuid;
    }

    public String generateTokenWithHeader(String username, String role, UUID uuid) {
        Calendar calendar = Calendar.getInstance();

        Date now = calendar.getTime();
        calendar.add(Calendar.MINUTE, JwtDefaults.EXPIRATION_MINUTES);
        Date expiration = calendar.getTime();

        Map<String, Object> claims = Map.of(
                JwtDefaults.USERNAME_CLAIM, username,
                JwtDefaults.ROLE_CLAIM, role,
                JwtDefaults.ID_CLAIM, uuid
        );

        return JwtDefaults.HEADER_PREFIX + Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(decodeSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(decodeSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey decodeSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JwtDefaults.BEARER_SECRET_HS512);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
