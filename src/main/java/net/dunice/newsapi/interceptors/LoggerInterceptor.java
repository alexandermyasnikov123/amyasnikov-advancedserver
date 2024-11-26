package net.dunice.newsapi.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.dunice.newsapi.services.LoggerService;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
public class LoggerInterceptor implements HandlerInterceptor {
    private final LoggerService service;

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            Exception ex
    ) {
        String requestUrl = request.getRequestURL().toString();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String httpMethod = request.getMethod();

        Integer responseStatus = response.getStatus();

        String message = String.format("%d: %s - %s; token = %s", responseStatus, httpMethod, requestUrl, token);
        String level = responseStatus >= HttpStatus.BAD_REQUEST.value() ? LogLevel.ERROR.name() : LogLevel.INFO.name();

        service.saveLog(message, level);
    }
}
