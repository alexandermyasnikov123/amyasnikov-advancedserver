package net.dunice.newsapi.security.impls;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import net.dunice.newsapi.configurations.JwtConfiguration;
import net.dunice.newsapi.security.JwtService;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtConfiguration jwtConfiguration;

    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(decodeSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(jwtConfiguration.getUsernameClaim(), String.class);
    }

    @Override
    public Boolean isTokenValid(String token, String username, String role, UUID uuid) {
        Claims claims = extractClaims(token);

        Boolean isNotExpired = claims.getExpiration().after(new Date());
        Boolean validUsername = claims.get(jwtConfiguration.getUsernameClaim()).equals(username);
        Boolean validRole = claims.get(jwtConfiguration.getRoleClaim()).equals(role);
        Boolean validUuid = claims.get(jwtConfiguration.getUuidClaim()).equals(uuid.toString());

        return isNotExpired && validUsername && validRole && validUuid;
    }

    @Override
    public String generateToken(String username, String role, UUID uuid) {
        Calendar calendar = Calendar.getInstance();

        Date now = calendar.getTime();
        calendar.add(Calendar.MINUTE, jwtConfiguration.getExpirationMinutes());
        Date expiration = calendar.getTime();

        Map<String, Object> claims = createClaims(username, role, uuid);

        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(decodeSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public String generateTokenWithHeader(String username, String role, UUID uuid) {
        String bearerPrefix = "Bearer ";
        return bearerPrefix + generateToken(username, role, uuid);
    }

    private Map<String, Object> createClaims(String username, String role, UUID uuid) {
        return Map.of(
                jwtConfiguration.getUsernameClaim(), username,
                jwtConfiguration.getRoleClaim(), role,
                jwtConfiguration.getUuidClaim(), uuid
        );
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(decodeSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey decodeSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfiguration.getBearerSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
