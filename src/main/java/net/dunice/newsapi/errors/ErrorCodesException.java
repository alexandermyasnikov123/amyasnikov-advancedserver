package net.dunice.newsapi.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dunice.newsapi.constants.ErrorCodes;

@Getter
@RequiredArgsConstructor
public class ErrorCodesException extends RuntimeException {
    private final ErrorCodes errorCodes;
}
