package net.dunice.newsapi.interceptors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import net.dunice.features.users.utils.AuthenticationUtils;
import net.dunice.newsapi.entities.LogEntity;
import net.dunice.newsapi.impl.HttpServletResponseUtils;
import net.dunice.newsapi.repositories.LogsRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class LoggerInterceptor implements HandlerInterceptor {
    private final LogsRepository repository;

    private final ObjectMapper mapper;

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            Exception ex
    ) {

        LogEntity.LogEntityBuilder builder = LogEntity.builder()
                .requestMethod(request.getMethod())
                .responseCode(response.getStatus())
                .endpoint(request.getRequestURL().toString())
                .requireAuth(request.getHeader(HttpHeaders.AUTHORIZATION) != null)
                .user(AuthenticationUtils.getCurrentUser());

        HttpServletResponseUtils.findCachedResponseBody(response).ifPresent(body -> {
            ErrorCodes errorCodes = findErrorCodesFromResponseBody(body);
            builder.errorCodesMessage(errorCodes.getMessage()).errorCodesStatus(errorCodes.getStatusCode());
        });

        repository.save(builder.build());
    }

    private ErrorCodes findErrorCodesFromResponseBody(String body) {
        try {
            BaseSuccessResponse model = mapper.readValue(body, BaseSuccessResponse.class);
            return ErrorCodes
                    .findEntriesByStatusCodes(Stream.of(model.getStatusCode()))
                    .findFirst()
                    .orElseThrow();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
