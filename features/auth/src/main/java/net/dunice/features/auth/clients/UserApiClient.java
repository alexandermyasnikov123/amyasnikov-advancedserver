package net.dunice.features.auth.clients;

import net.dunice.features.auth.dtos.requests.RegisterRequest;
import net.dunice.features.auth.dtos.responses.UserResponse;
import net.dunice.features.core.dtos.responses.common.CustomSuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "users", url = "http://localhost:8081/api/v1/user")
public interface UserApiClient {
    @PostMapping
    CustomSuccessResponse<UserResponse> insertUser(@RequestBody RegisterRequest request);

    @GetMapping(value = "email/{email}")
    CustomSuccessResponse<UserResponse> loadByEmail(@PathVariable String email);
}
