package net.dunice.features.core.dtos.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;

@Getter
@RequiredArgsConstructor
public class ErrorCodesException extends RuntimeException {
    private final ErrorCodes errorCodes;
}
