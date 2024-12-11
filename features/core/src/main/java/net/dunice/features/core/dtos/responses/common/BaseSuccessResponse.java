package net.dunice.features.core.dtos.responses.common;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
public class BaseSuccessResponse {
    private Integer statusCode = 1;

    private Boolean success = true;

    private List<Integer> codes;

    @Getter(onMethod_ = @JsonAnyGetter)
    private final Map<String, Object> payload = new HashMap<>();

    public BaseSuccessResponse() {
    }

    public BaseSuccessResponse(List<Integer> codes) {
        this.statusCode = codes.stream().min(Integer::compareTo).orElseThrow();
        this.codes = codes;
    }

    public BaseSuccessResponse(Integer... codes) {
        this(Arrays.stream(codes).toList());
    }

    public BaseSuccessResponse addPayload(String key, Object value) {
        payload.put(key, value);
        return this;
    }
}
