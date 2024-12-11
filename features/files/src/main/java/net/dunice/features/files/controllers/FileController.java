package net.dunice.features.files.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import net.dunice.features.files.services.FilesService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;

@RestController
@RequestMapping(value = "file")
@RequiredArgsConstructor
public class FileController {
    private final FilesService service;

    @GetMapping(value = "{path}")
    public ResponseEntity<Resource> loadFileByPath(@PathVariable String path) throws MalformedURLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                .body(service.loadFile(path));
    }

    @PostMapping(value = "uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseSuccessResponse> uploadFile(
            @RequestParam MultipartFile file,
            HttpServletRequest request
    ) throws Exception {
        return ResponseEntity.ok(service.storeFile(file, request));
    }
}
