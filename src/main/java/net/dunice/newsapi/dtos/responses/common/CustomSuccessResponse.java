package net.dunice.newsapi.dtos.responses.common;

import lombok.EqualsAndHashCode;
import lombok.Value;
import net.dunice.newsapi.errors.ErrorCodes;

@Value
@EqualsAndHashCode(callSuper = true)
public class CustomSuccessResponse<T> extends BaseSuccessResponse {
    T data;

    public CustomSuccessResponse(T data) {
        super(ErrorCodes.SUCCESS.getStatusCode(), null, null);
        this.data = data;
    }
}
