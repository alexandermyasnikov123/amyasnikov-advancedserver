package net.dunice.features.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dunice.features.auth.constants.JwtDefaults;
import net.dunice.features.users.entities.UserEntityDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Boolean hasBearerToken = authHeader != null && authHeader.startsWith(JwtDefaults.HEADER_PREFIX);

        if (!hasBearerToken || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(JwtDefaults.HEADER_PREFIX.length());
        createAuthTokenIfValid(jwtToken).ifPresent(authToken -> {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);

            SecurityContextHolder.setContext(context);
        });

        filterChain.doFilter(request, response);
    }

    private Optional<AbstractAuthenticationToken> createAuthTokenIfValid(String jwtToken) {
        String username = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Boolean isValid = userDetails instanceof UserEntityDetails entity ?
                jwtService.isTokenValid(jwtToken, userDetails.getUsername(), entity.getRole(), entity.getId()) :
                false;

        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        return isValid ? Optional.of(authToken) : Optional.empty();
    }
}
