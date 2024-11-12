package net.dunice.newsapi.utils.impl;

import com.googlecode.pngtastic.core.PngImage;
import com.googlecode.pngtastic.core.PngOptimizer;
import net.dunice.newsapi.utils.MultipartFileDataStore;
import net.dunice.newsapi.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@SuppressWarnings("WrapperTypeMayBePrimitive")
public class PngOptimizerDataStoreImpl implements MultipartFileDataStore {
    private static final DateFormat fileDatePattern = new SimpleDateFormat("hh_mm_ss_ms_dd_MM_yyyy");

    private final String outputDirectoryPath;

    public PngOptimizerDataStoreImpl(@Value("${images-classpath}") String outputDirectoryPath) {
        this.outputDirectoryPath = StringUtils.withoutTrailingSlash(outputDirectoryPath);
    }

    @Override
    public URI compressAndStore(MultipartFile file) throws IOException {
        URI outputUri = URI.create(createOutputName());

        Boolean removeGamma = true;
        Integer compressionLevel = 95;

        PngOptimizer optimizer = new PngOptimizer();
        PngImage outputImage = optimizer.optimize(new PngImage(file.getInputStream()), removeGamma, compressionLevel);

        try (OutputStream outputStream = Files.newOutputStream(Paths.get(outputUri))) {
            outputImage.writeDataOutputStream(outputStream);
        }

        return outputUri;
    }

    private String createOutputName() {
        return outputDirectoryPath + fileDatePattern.format(new Date());
    }
}
