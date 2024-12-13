package net.dunice.newsapi.errors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.constants.ValidationMessages;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Stream;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = HttpStatusCodeException.class)
    public ResponseEntity<BaseSuccessResponse> handleStatusCodeExceptions(HttpStatusCodeException exception) {
        return buildErrorResponse(Stream.of(exception.getStatusText()));
    }

    /*@ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<BaseSuccessResponse> handleAuthenticationException() {
        return buildErrorResponse(Stream.of(ValidationMessages.PASSWORD_NOT_VALID));
    }*/

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<BaseSuccessResponse> handleFileNotFoundExceptions() {
        return buildErrorResponse(Stream.of(ValidationMessages.EXCEPTION_HANDLER_NOT_PROVIDED));
    }

    @ExceptionHandler(value = FileUploadException.class)
    public ResponseEntity<BaseSuccessResponse> handleFileUploadExceptions() {
        return buildErrorResponse(Stream.of(ValidationMessages.UNKNOWN));
    }

    @ExceptionHandler(value = ErrorCodesException.class)
    public ResponseEntity<BaseSuccessResponse> handleErrorCodesExceptions(ErrorCodesException exception) {
        return buildErrorResponse(exception.getErrorCodes().stream().map(ErrorCodes::getMessage));
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

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<BaseSuccessResponse> handleRequestParamsException(
            MissingServletRequestParameterException exception
    ) {
        return buildErrorResponse(Stream.of(exception.getParameterName()));
    }

    private ResponseEntity<BaseSuccessResponse> buildErrorResponse(Stream<String> messages) {
        List<Integer> statusCodes = ErrorCodes
                .findEntriesByMessages(messages)
                .map(ErrorCodes::getStatusCode)
                .toList();

        return ResponseEntity.badRequest().body(new BaseSuccessResponse(statusCodes));
    }
}
