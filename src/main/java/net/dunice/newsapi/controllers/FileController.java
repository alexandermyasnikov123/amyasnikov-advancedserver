package net.dunice.newsapi.controllers;

import lombok.AllArgsConstructor;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.services.FilesService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;
import java.net.URI;

@RestController
@RequestMapping(value = "file")
@AllArgsConstructor
public class FileController {
    private final FilesService service;

    @GetMapping(value = "{path}", produces = "image/jpg")
    public ResponseEntity<Resource> loadFileByPath(@PathVariable String path) throws MalformedURLException {
        URI uri = URI.create(path);
        return ResponseEntity.ok(service.loadFile(uri.getPath()));
    }

    @PostMapping(value = "uploadFile")
    public ResponseEntity<BaseSuccessResponse> uploadFile(@RequestParam MultipartFile file) throws Exception {
        return ResponseEntity.ok(service.storeFile(file));
    }
}
