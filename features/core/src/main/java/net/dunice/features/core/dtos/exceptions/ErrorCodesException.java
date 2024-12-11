package net.dunice.features.core.dtos.exceptions;

import lombok.Getter;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import java.util.Collection;
import java.util.List;

@Getter
public class ErrorCodesException extends RuntimeException {
    private final Collection<ErrorCodes> errorCodes;

    public ErrorCodesException(Collection<ErrorCodes> errorCodes) {
        super(joinErrorCodesToString(errorCodes));
        this.errorCodes = errorCodes;
    }

    public ErrorCodesException(ErrorCodes... errorCodes) {
        this(List.of(errorCodes));
    }

    private static String joinErrorCodesToString(Collection<ErrorCodes> errorCodes) {
        return String.join(", ", errorCodes.stream()
                .map(it -> it.getStatusCode() + "-" + it.getMessage())
                .toList()
        );
    }
}
