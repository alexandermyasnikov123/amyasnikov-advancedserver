package net.dunice.newsapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.Setter;
import net.dunice.newsapi.services.UserAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtSecurityFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Setter(onMethod_ = {@Autowired, @Lazy})
    private UserAuthService userAuthService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(BEARER_PREFIX.length());
        Boolean isNotAuthenticated = SecurityContextHolder.getContext().getAuthentication() == null;

        if (isNotAuthenticated) {
            AbstractAuthenticationToken authToken = userAuthService.generateAuthToken(jwtToken);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);

            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }
}
