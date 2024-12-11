package net.dunice.features.core.dtos.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import net.dunice.features.core.dtos.responses.common.CustomSuccessResponse;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseUtils {

    public static <T> T tryExtractData(BaseSuccessResponse response) {
        System.out.println("Response came here: " + response);
        return switch (response) {
            case CustomSuccessResponse<?> successResponse -> (T) successResponse.getData();
            case BaseSuccessResponse baseResponse when !CollectionUtils.isNullOrEmpty(baseResponse.getCodes()) -> {
                Stream<ErrorCodes> errorCodes = ErrorCodes.findEntriesByStatusCodes(baseResponse.getCodes().stream());
                throw new ErrorCodesException(errorCodes.toList());
            }
            default -> throw new IllegalArgumentException("response must be a CustomSuccessResponse non-null object");
        };
    }
}
