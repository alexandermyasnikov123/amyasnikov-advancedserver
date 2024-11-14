package net.dunice.newsapi.security.impls;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.dunice.newsapi.constants.JwtFieldsConstants;
import net.dunice.newsapi.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;

@Service
public class JwtServiceImpl implements JwtService {
    @Value(value = "${bearer-secret}")
    private String signingKey;

    @Value(value = "${expiration-minutes}")
    private Integer expirationMinutes;

    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(decodeSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(JwtFieldsConstants.Fields.username, String.class);
    }

    @Override
    public Boolean isTokenValid(String token, String username, String role, UUID uuid) {
        Claims claims = extractClaims(token);

        Boolean isNotExpired = claims.getExpiration().after(new Date());
        Boolean validUsername = claims.get(JwtFieldsConstants.Fields.username).equals(username);
        Boolean validRole = claims.get(JwtFieldsConstants.Fields.role).equals(role);
        Boolean validUuid = claims.get(JwtFieldsConstants.Fields.uuid).equals(uuid.toString());

        return isNotExpired && validUsername && validRole && validUuid;
    }

    @Override
    public String generateToken(String username, String role, UUID uuid) {
        Calendar calendar = Calendar.getInstance();

        Date now = calendar.getTime();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        Map<String, Object> claims = createClaims(username, role, uuid);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(decodeSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private Map<String, Object> createClaims(String username, String role, UUID uuid) {
        return Map.of(
                JwtFieldsConstants.Fields.username, username,
                JwtFieldsConstants.Fields.role, role,
                JwtFieldsConstants.Fields.uuid, uuid
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
        byte[] keyBytes = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
