package net.dunice.features.auth.clients;

import net.dunice.features.auth.dtos.requests.RegisterRequest;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "users")
public interface UserApiClient {
    @PostMapping
    BaseSuccessResponse insertUser(@RequestBody RegisterRequest request);

    @GetMapping(value = "email")
    BaseSuccessResponse loadByEmail(String email);
}
