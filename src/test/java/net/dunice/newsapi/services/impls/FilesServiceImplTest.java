package net.dunice.newsapi.services.impls;

import jakarta.servlet.http.HttpServletRequest;
import net.dunice.newsapi.BaseTestCase;
import net.dunice.features.news.constants.ErrorCodes;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.services.defaults.FilesTestConstants;
import net.dunice.newsapi.utils.MultipartFileDataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilesServiceImplTest extends BaseTestCase {
    @Mock
    private MultipartFileDataStore dataStore;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private HttpServletRequest request;

    private FilesServiceImpl service;

    @BeforeEach
    public void beforeEach() {
        service = new FilesServiceImpl(dataStore);
    }

    @Test
    public void storeFile_ThrowsErrorCodesExceptionIfFileIsEmpty() throws IOException {
        when(multipartFile.isEmpty()).thenReturn(true);

        ErrorCodesException actual = assertThrows(
                ErrorCodesException.class,
                () -> service.storeFile(multipartFile, request)
        );

        InOrder inOrder = inOrder(multipartFile, dataStore);

        inOrder.verify(multipartFile).isEmpty();
        inOrder.verify(dataStore, never()).compressAndStore(any(), anyString());

        assertEquals(ErrorCodes.EMPTY_MULTIPART_FILE, actual.getErrorCodes());
    }

    @Test
    public void storeFile_CompressAndStoreFileIfValid() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(false);

        when(dataStore.compressAndStore(any(), anyString()))
                .thenReturn(FilesTestConstants.FILE_PATH);

        BaseSuccessResponse actual = service.storeFile(multipartFile, request);

        InOrder inOrder = inOrder(multipartFile, dataStore);

        inOrder.verify(multipartFile).isEmpty();
        inOrder.verify(dataStore).compressAndStore(any(), anyString());

        assertEquals(FilesTestConstants.SUCCESS_FILE_RESPONSE, actual);
    }

    @Test
    public void loadFile_DelegateCallToTheDataStore() throws Exception {
        Resource resource = mock();

        when(dataStore.loadCompressedFile(FilesTestConstants.FILE_PATH)).thenReturn(resource);

        dataStore.loadCompressedFile(FilesTestConstants.FILE_PATH);

        verify(dataStore).loadCompressedFile(FilesTestConstants.FILE_PATH);
    }
}