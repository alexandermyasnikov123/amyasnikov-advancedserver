package net.dunice.newsapi.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dunice.newsapi.constants.ErrorCodes;

@AllArgsConstructor
@Getter
public class ErrorCodesException extends RuntimeException {
    private final ErrorCodes errorCodes;
}
