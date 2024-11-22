package net.dunice.newsapi.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggerInterceptor implements HandlerInterceptor {
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

        log.atInfo().log(String.format("%d: %s - %s; token = %s", responseStatus, httpMethod, requestUrl, token));
    }
}
