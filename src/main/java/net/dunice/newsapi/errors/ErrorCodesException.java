package net.dunice.newsapi.errors;

import lombok.Getter;
import net.dunice.newsapi.constants.ErrorCodes;

@Getter
public class ErrorCodesException extends RuntimeException {
    private final ErrorCodes errorCodes;

    public ErrorCodesException(ErrorCodes errorCodes) {
        super(errorCodes.getMessage());
        this.errorCodes = errorCodes;
    }
}
