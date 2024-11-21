package net.dunice.newsapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.dunice.newsapi.services.AuthService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

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
        tryAuthenticate(request);
        filterChain.doFilter(request, response);
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    private void tryAuthenticate(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        Boolean hasBearerToken = authHeader != null && authHeader.startsWith(BEARER_PREFIX);

        if (!hasBearerToken || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        String jwtToken = authHeader.substring(BEARER_PREFIX.length());
        service.createAuthenticationTokenIfValid(jwtToken).ifPresent(authToken -> {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);

            SecurityContextHolder.setContext(context);
        });
    }
}
