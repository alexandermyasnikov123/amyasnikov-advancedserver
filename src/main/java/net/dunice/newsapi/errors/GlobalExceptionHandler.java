package net.dunice.newsapi.errors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.constants.ValidationConstants;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Map<String, ErrorCodes> errors = Arrays.stream(ErrorCodes.values())
            .collect(Collectors.toMap(ErrorCodes::getMessage, errorCodes -> errorCodes));

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<BaseSuccessResponse> handleAuthenticationException() {
        return buildErrorResponse(Stream.of(ValidationConstants.PASSWORD_NOT_VALID));
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<BaseSuccessResponse> handleFileNotFoundExceptions() {
        return buildErrorResponse(Stream.of(ValidationConstants.EXCEPTION_HANDLER_NOT_PROVIDED));
    }

    @ExceptionHandler(value = FileUploadException.class)
    public ResponseEntity<BaseSuccessResponse> handleFileUploadExceptions() {
        return buildErrorResponse(Stream.of(ValidationConstants.UNKNOWN));
    }

    @ExceptionHandler(value = ErrorCodesException.class)
    public ResponseEntity<BaseSuccessResponse> handleErrorCodesExceptions(ErrorCodesException exception) {
        return buildErrorResponse(Stream.of(exception.getErrorCodes().getMessage()));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<BaseSuccessResponse> handleConstraintsExceptions(ConstraintViolationException exception) {
        Stream<String> messages = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage);

        return buildErrorResponse(messages);
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<BaseSuccessResponse> handleBindExceptions(BindException exception) {
        Stream<String> messages = exception.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage);

        return buildErrorResponse(messages);
    }

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public ResponseEntity<BaseSuccessResponse> handleHandlerExceptions(HandlerMethodValidationException exception) {
        return buildErrorResponse(exception.getAllErrors().stream().map(MessageSourceResolvable::getDefaultMessage));
    }

    private ResponseEntity<BaseSuccessResponse> buildErrorResponse(Stream<String> messages) {
        Integer[] statusCodes = messages.map(message ->
                errors.getOrDefault(message, ErrorCodes.UNKNOWN).getStatusCode()
        ).toList().toArray(Integer[]::new);

        return ResponseEntity.badRequest().body(BaseSuccessResponse.error(statusCodes));
    }
}
