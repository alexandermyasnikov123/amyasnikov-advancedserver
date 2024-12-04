package net.dunice.newsapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ContentCachingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var cachedResponse = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(request, cachedResponse);
        } finally {
            cachedResponse.copyBodyToResponse();
        }
    }
}
