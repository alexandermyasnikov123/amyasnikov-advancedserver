package net.dunice.features.core.dtos.responses.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomSuccessResponse<T> extends BaseSuccessResponse {
    private T data;
}
