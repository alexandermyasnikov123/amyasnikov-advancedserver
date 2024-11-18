package net.dunice.newsapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.dunice.newsapi.services.AuthService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final AuthService service;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> jwtToken = tryExtractBearerToken(request);
        jwtToken.ifPresent(token -> filterRequest(token, request));

        filterChain.doFilter(request, response);
    }

    private Optional<String> tryExtractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        Supplier<String> token = () -> authHeader.substring(BEARER_PREFIX.length());
        Supplier<Boolean> isTokenValid = () -> service.isTokenValid(token.get());
        Boolean hasBearerToken = authHeader != null && authHeader.startsWith(BEARER_PREFIX);

        return hasBearerToken && isTokenValid.get() ? Optional.of(token.get()) : Optional.empty();
    }

    private void filterRequest(String jwtToken, HttpServletRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            AbstractAuthenticationToken authToken = service.generateAuthToken(jwtToken);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);

            SecurityContextHolder.setContext(context);
        }
    }
}
