package net.dunice.features.users.clients;

import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "files", url = "http://localhost:8083/api/v1/files")
public interface FilesApiClient {

    @DeleteMapping
    BaseSuccessResponse deleteFileByUrl(@RequestParam String url);
}
