package net.dunice.newsapi.services.impls;

import lombok.AllArgsConstructor;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.utils.MultipartFileDataStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;

@Service
@AllArgsConstructor
@SuppressWarnings("WrapperTypeMayBePrimitive")
public class FilesServiceImpl {
    private final MultipartFileDataStore dataStore;

    public Resource storeFile(MultipartFile file) throws IOException {
        Integer length = file.getName().length();

        if (file.isEmpty() || length < UserEntity.MIN_AVATAR_LENGTH || length > UserEntity.MAX_AVATAR_LENGTH) {
            throw new ErrorCodesException(ErrorCodes.USER_AVATAR_NOT_VALID);
        }

        URI outputUri = dataStore.compressAndStore(file);
        return new UrlResource(outputUri);
    }
}
