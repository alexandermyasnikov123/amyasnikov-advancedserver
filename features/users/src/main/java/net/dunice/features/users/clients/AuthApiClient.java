package net.dunice.features.users.clients;

import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "auth")
public interface AuthApiClient {
    @GetMapping("current")
    BaseSuccessResponse loadCurrentPrincipal();
}
