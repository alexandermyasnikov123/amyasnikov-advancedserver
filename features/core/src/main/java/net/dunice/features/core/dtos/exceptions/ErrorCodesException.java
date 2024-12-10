package net.dunice.features.core.dtos.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ErrorCodesException extends RuntimeException {
    private final Collection<ErrorCodes> errorCodes;

    public ErrorCodesException(ErrorCodes... errorCodes) {
        this(List.of(errorCodes));
    }
}
