package net.dunice.newsapi.security.impls;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.dunice.newsapi.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

@Service
public class JwtServiceImpl implements JwtService {
    @Value(value = "${bearer-secret}")
    private String signingKey;

    @Value(value = "${username-claim}")
    private String usernameClaim;

    @Value(value = "${expiration-minutes}")
    private Integer expirationMinutes;

    @Override
    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(decodeSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get(usernameClaim, String.class);
    }

    @Override
    public String generateToken(String username) {
        return generateToken(username, Map.of());
    }

    @Override
    public String generateToken(String username, Map<String, Object> extraClaims) {
        Calendar calendar = Calendar.getInstance();

        Date now = calendar.getTime();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return Jwts.builder().claims(extraClaims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(decodeSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey decodeSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
