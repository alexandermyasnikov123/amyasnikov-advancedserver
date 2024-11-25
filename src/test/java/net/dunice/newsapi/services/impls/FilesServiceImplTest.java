package net.dunice.newsapi.services.impls;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.utils.MultipartFileDataStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.concurrent.atomic.AtomicBoolean;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilesServiceImplTest {
    MultipartFileDataStore dataStore = Mockito.mock(MultipartFileDataStore.class);

    Resource mockResource = Mockito.mock(Resource.class);

    MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);

    @NonFinal
    FilesServiceImpl service;

    @BeforeEach
    public void beforeEach() {
        service = new FilesServiceImpl(dataStore);
    }

    @Test
    public void storeFile_ThrowsErrorCodesExceptionIfFileIsEmpty() {
        Mockito.when(mockMultipartFile.isEmpty()).thenReturn(true);

        ErrorCodesException exception = Assertions.assertThrows(ErrorCodesException.class, () -> {
            service.storeFile(mockMultipartFile, "path");
        });

        Assertions.assertEquals(ErrorCodes.EMPTY_MULTIPART_FILE, exception.getErrorCodes());
    }

    @Test
    public void storeFile_CompressAndStoreFileIfValid() throws Exception {
        Mockito.when(mockMultipartFile.isEmpty()).thenReturn(false);

        AtomicBoolean compressAndStoreWasCalled = new AtomicBoolean(false);

        Mockito.when(dataStore.compressAndStore(Mockito.any(), Mockito.anyString()))
                .then(invocation -> {
                    compressAndStoreWasCalled.set(true);
                    return "path_to_file";
                });

        BaseSuccessResponse response = service.storeFile(mockMultipartFile, "path");
        BaseSuccessResponse expected = new CustomSuccessResponse<>("path_to_file");

        Assertions.assertTrue(compressAndStoreWasCalled.get());
        Assertions.assertEquals(expected, response);
    }

    @Test
    public void loadFile_DelegateCallToTheDataStore() throws Exception {
        try {
            AtomicBoolean dataStoreCalled = new AtomicBoolean(false);
            Mockito.when(dataStore.loadCompressedFile(Mockito.anyString())).then(invocation -> {
                dataStoreCalled.set(true);
                return mockResource;
            });
            Resource actual = service.loadFile(Mockito.anyString());
            Assertions.assertEquals(mockResource, actual);
            Assertions.assertTrue(dataStoreCalled.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}