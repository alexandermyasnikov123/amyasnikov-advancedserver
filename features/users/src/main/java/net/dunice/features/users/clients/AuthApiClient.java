package net.dunice.features.users.clients;

import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import net.dunice.features.core.dtos.responses.common.CustomSuccessResponse;
import net.dunice.features.users.dtos.responses.CredentialsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "auth", url = "http://localhost:8082/api/v1/auth")
public interface AuthApiClient {

    @GetMapping("current")
    CustomSuccessResponse<CredentialsResponse> loadCurrentPrincipal(@RequestHeader HttpHeaders headers);

    @DeleteMapping("current")
    BaseSuccessResponse deleteCurrentPrincipal(@RequestHeader HttpHeaders headers);
}
