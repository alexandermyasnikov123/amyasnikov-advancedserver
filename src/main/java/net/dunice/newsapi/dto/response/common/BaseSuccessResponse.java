package net.dunice.newsapi.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import net.dunice.newsapi.errors.ErrorCodes;
import java.util.Collection;
import java.util.Date;

@Value
@NonFinal
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseSuccessResponse {
    Integer statusCode;

    Boolean success = true;

    @Nullable
    Date timeStamp;

    @Nullable
    Iterable<Integer> codes;

    public static BaseSuccessResponse success() {
        return new BaseSuccessResponse(ErrorCodes.SUCCESS.getStatusCode(), null, null);
    }

    public static BaseSuccessResponse error(Collection<Integer> codes) {
        Integer statusCode = codes.stream().findFirst().orElseThrow();
        Date now = new Date();
        return new BaseSuccessResponse(statusCode, now, codes);
    }
}
