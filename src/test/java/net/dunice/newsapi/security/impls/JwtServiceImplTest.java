package net.dunice.newsapi.security.impls;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.dunice.newsapi.configurations.JwtConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImplTest {
    JwtConfiguration configuration = new JwtConfiguration()
            .setBearerSecret("8b8584852d185261d9505563cb4ab7cfabf0a6e6485aa8f0e5664a84396e25de5cb888636a057433861e5085806a96557356cf5ab0cc2c8aa712ea66ccce83f7")
            .setUsernameClaim("username")
            .setRoleClaim("role")
            .setUuidClaim("uuid")
            .setExpirationMinutes(10);

    @NonFinal
    JwtServiceImpl jwtService;

    @BeforeEach
    public void beforeEach() {
        jwtService = new JwtServiceImpl(configuration);
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

        Assertions.assertEquals(username, claims.get(configuration.getUsernameClaim()));
        Assertions.assertEquals(role, claims.get(configuration.getRoleClaim()));
        Assertions.assertEquals(uuid.toString(), claims.get(configuration.getUuidClaim()));
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
        byte[] keyBytes = Decoders.BASE64.decode(configuration.getBearerSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}