package net.dunice.newsapi.security.impls;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.dunice.newsapi.constants.JwtDefaults;
import net.dunice.newsapi.security.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceTest {
    @NonFinal
    JwtService jwtService;

    @BeforeEach
    public void beforeEach() {
        jwtService = new JwtService();
    }

    @Test
    public void generateToken_ReturnsValidTokenWith10MinutesTimeout() {
        Calendar calendar = Calendar.getInstance();

        String username = "my_username_1";
        String role = "user";
        UUID uuid = UUID.randomUUID();
        calendar.add(Calendar.MINUTE, 10);
        Integer expirationMinutes = 10;

        String token = jwtService.generateToken(username, role, uuid);
        Claims claims = extractClaims(token);

        Long actualExpirationMillis = (Long) claims.get(Claims.EXPIRATION) - (Long) claims.get(Claims.ISSUED_AT);
        Long actualExpirationMinutes = TimeUnit.SECONDS.toMinutes(actualExpirationMillis);

        Assertions.assertEquals(username, claims.get(JwtDefaults.USERNAME_CLAIM));
        Assertions.assertEquals(role, claims.get(JwtDefaults.ROLE_CLAIM));
        Assertions.assertEquals(uuid.toString(), claims.get(JwtDefaults.ID_CLAIM));
        Assertions.assertEquals(expirationMinutes.intValue(), actualExpirationMinutes.intValue());

        Assertions.assertTrue(jwtService.isTokenValid(token, username, role, uuid));
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