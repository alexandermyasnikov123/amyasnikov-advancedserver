package net.dunice.newsapi.services.defaults;

import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;

public interface FilesTestConstants {

    String FILE_PATH = "base_path/path_to_file";

    CustomSuccessResponse<String> SUCCESS_FILE_RESPONSE = new CustomSuccessResponse<>(FILE_PATH);
}
