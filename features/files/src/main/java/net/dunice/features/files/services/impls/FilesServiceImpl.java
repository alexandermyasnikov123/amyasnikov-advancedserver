package net.dunice.features.files.services.impls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dunice.features.core.dtos.constants.ErrorCodes;
import net.dunice.features.core.dtos.exceptions.ErrorCodesException;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import net.dunice.features.core.dtos.responses.common.CustomSuccessResponse;
import net.dunice.features.files.datastore.MultipartFileDataStore;
import net.dunice.features.files.services.FilesService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.MalformedURLException;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {
    private final MultipartFileDataStore dataStore;

    @Override
    public BaseSuccessResponse storeFile(MultipartFile file, HttpServletRequest request) throws Exception {
        if (file.isEmpty()) {
            throw new ErrorCodesException(ErrorCodes.EMPTY_MULTIPART_FILE);
        }

        String baseApiPath = ServletUriComponentsBuilder
                .fromContextPath(request)
                .toUriString();

        String resultingPathUrl = dataStore.compressAndStore(file, baseApiPath);
        return new CustomSuccessResponse<>(resultingPathUrl);
    }

    @Override
    public Resource loadFile(String filename) throws MalformedURLException {
        return dataStore.loadCompressedFile(filename);
    }

    @Override
    public void deleteFileByUrl(String url) {
        String fileName = Paths.get(url).getFileName().toString();
        dataStore.deleteFileByName(fileName);
    }
}
