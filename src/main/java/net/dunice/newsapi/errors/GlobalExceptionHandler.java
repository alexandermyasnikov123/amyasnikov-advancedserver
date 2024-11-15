package net.dunice.newsapi.errors;

import jakarta.validation.ConstraintViolationException;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Map<String, ErrorCodes> errors = Arrays.stream(ErrorCodes.values())
            .collect(Collectors.toMap(ErrorCodes::getMessage, errorCodes -> errorCodes));

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<BaseSuccessResponse> handleAuthenticationException(BadCredentialsException ignored) {
        return ResponseEntity
                .badRequest()
                .body(BaseSuccessResponse.error(ErrorCodes.USER_PASSWORD_NOT_VALID.getStatusCode()));
    }

    @ExceptionHandler(value = ErrorCodesException.class)
    public ResponseEntity<BaseSuccessResponse> handleErrorCodesExceptions(ErrorCodesException exception) {
        Integer statusCode = exception.getErrorCodes().getStatusCode();
        return ResponseEntity
                .badRequest()
                .body(BaseSuccessResponse.error(statusCode));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<BaseSuccessResponse> handleConstraintsExceptions(ConstraintViolationException exception) {
        List<Integer> codes = exception.getConstraintViolations().stream().map(violation ->
                errors.getOrDefault(violation.getMessage(), ErrorCodes.UNKNOWN).getStatusCode()
        ).toList();

        return ResponseEntity
                .badRequest()
                .body(BaseSuccessResponse.error(codes));
    }
}
