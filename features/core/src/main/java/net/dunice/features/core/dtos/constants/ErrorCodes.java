package net.dunice.features.core.dtos.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum ErrorCodes {
    UNKNOWN(0, ValidationMessages.UNKNOWN),

    USERNAME_SIZE_NOT_VALID(1, ValidationMessages.USERNAME_SIZE_NOT_VALID),

    ROLE_SIZE_NOT_VALID(2, ValidationMessages.ROLE_SIZE_NOT_VALID),

    EMAIL_SIZE_NOT_VALID(3, ValidationMessages.EMAIL_SIZE_NOT_VALID),

    MUST_NOT_BE_NULL(4, ValidationMessages.MUST_NOT_BE_NULL),

    USER_NOT_FOUND(5, ValidationMessages.USER_NOT_FOUND),

    TOKEN_NOT_PROVIDED(6, ValidationMessages.TOKEN_NOT_PROVIDED),

    UNAUTHORISED(7, ValidationMessages.UNAUTHORISED),

    USER_EMAIL_NOT_NULL(8, ValidationMessages.USER_EMAIL_NOT_NULL),

    USER_ROLE_NOT_NULL(10, ValidationMessages.USER_ROLE_NOT_NULL),

    NEWS_DESCRIPTION_SIZE(11, ValidationMessages.NEWS_DESCRIPTION_SIZE),

    NEWS_DESCRIPTION_NOT_NULL(12, ValidationMessages.NEWS_DESCRIPTION_NOT_NULL),

    NEWS_TITLE_SIZE(13, ValidationMessages.NEWS_TITLE_SIZE),

    NEWS_TITLE_NOT_NULL(14, ValidationMessages.NEWS_TITLE_NOT_NULL),

    USER_EMAIL_NOT_VALID(17, ValidationMessages.USER_EMAIL_NOT_VALID),

    PAGE_SIZE_NOT_VALID(18, ValidationMessages.PAGE_SIZE_NOT_VALID),

    PER_PAGE_MIN_NOT_VALID(19, ValidationMessages.PER_PAGE_MIN_NOT_VALID),

    PER_PAGE_MAX_NOT_VALID(19, ValidationMessages.PER_PAGE_MAX_NOT_VALID),

    CODE_NOT_NULL(20, ValidationMessages.CODE_NOT_NULL),

    EXCEPTION_HANDLER_NOT_PROVIDED(21, ValidationMessages.EXCEPTION_HANDLER_NOT_PROVIDED),

    REQUEST_IS_NOT_MULTIPART(22, ValidationMessages.REQUEST_IS_NOT_MULTIPART),

    MAX_UPLOAD_SIZE_EXCEEDED(23, ValidationMessages.MAX_UPLOAD_SIZE_EXCEEDED),

    USER_AVATAR_NOT_NULL(24, ValidationMessages.USER_AVATAR_NOT_NULL),

    PASSWORD_NOT_VALID(25, ValidationMessages.PASSWORD_NOT_VALID),

    PASSWORD_NOT_NULL(26, ValidationMessages.PASSWORD_NOT_NULL),

    NEWS_NOT_FOUND(27, ValidationMessages.NEWS_NOT_FOUND),

    ID_MUST_BE_POSITIVE(29, ValidationMessages.ID_MUST_BE_POSITIVE),

    USER_ALREADY_EXISTS(30, ValidationMessages.USER_ALREADY_EXISTS),

    TODO_TEXT_NOT_NULL(31, ValidationMessages.TODO_TEXT_NOT_NULL),

    TODO_TEXT_SIZE_NOT_VALID(32, ValidationMessages.TODO_TEXT_SIZE_NOT_VALID),

    TODO_STATUS_NOT_NULL(33, ValidationMessages.TODO_STATUS_NOT_NULL),

    TASK_NOT_FOUND(34, ValidationMessages.TASK_NOT_FOUND),

    TASK_PATCH_UPDATED_NOT_CORRECT_COUNT(35, ValidationMessages.TASK_PATCH_UPDATED_NOT_CORRECT_COUNT),

    TASKS_PAGE_GREATER_OR_EQUAL_1(37, ValidationMessages.TASKS_PAGE_GREATER_OR_EQUAL_1),

    TASKS_PER_PAGE_GREATER_OR_EQUAL_1(38, ValidationMessages.TASKS_PER_PAGE_GREATER_OR_EQUAL_1),

    TASKS_PER_PAGE_LESS_OR_EQUAL_100(39, ValidationMessages.TASKS_PER_PAGE_LESS_OR_EQUAL_100),

    REQUIRED_INT_PARAM_PAGE_IS_NOT_PRESENT(40, ValidationMessages.REQUIRED_INT_PARAM_PAGE_IS_NOT_PRESENT),

    REQUIRED_INT_PARAM_PER_PAGE_IS_NOT_PRESENT(41, ValidationMessages.REQUIRED_INT_PARAM_PER_PAGE_IS_NOT_PRESENT),

    USER_NAME_HAS_TO_BE_PRESENT(43, ValidationMessages.USER_NAME_HAS_TO_BE_PRESENT),

    TAGS_NOT_VALID(44, ValidationMessages.TAGS_NOT_VALID),

    NEWS_IMAGE_HAS_TO_BE_PRESENT(45, ValidationMessages.NEWS_IMAGE_HAS_TO_BE_PRESENT),

    USER_WITH_THIS_EMAIL_ALREADY_EXIST(46, ValidationMessages.USER_WITH_THIS_EMAIL_ALREADY_EXIST),

    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(47, ValidationMessages.HTTP_MESSAGE_NOT_READABLE_EXCEPTION),

    EMPTY_MULTIPART_FILE(49, ValidationMessages.EMPTY_MULTIPART_FILE),

    USER_AVATAR_NOT_VALID(50, ValidationMessages.USER_AVATAR_NOT_VALID),

    USER_PASSWORD_NULL(51, ValidationMessages.USER_PASSWORD_NULL),

    USER_ID_NULL(52, ValidationMessages.USER_ID_NULL),

    USERNAME_NULL(53, ValidationMessages.USERNAME_NULL),

    USER_ROLE_NULL(54, ValidationMessages.USER_ROLE_NULL),

    TOKEN_POSITION_MISMATCH(55, ValidationMessages.TOKEN_POSITION_MISMATCH),

    NEWS_IMAGE_LENGTH(56, ValidationMessages.NEWS_IMAGE_LENGTH),

    NEWS_ID_NULL(57, ValidationMessages.NEWS_ID_NULL),

    INVALID_JWT_TOKEN(58, ValidationMessages.INVALID_JWT_TOKEN),

    NEWS_ID_MUST_BE_POSITIVE(59, ValidationMessages.NEWS_ID_MUST_BE_POSITIVE),

    CANT_MODIFY_FOREIGN_NEWS(60, ValidationMessages.CANT_MODIFY_FOREIGN_NEWS);

    private static final Map<String, ErrorCodes> MESSAGES_ENTRIES = Arrays.stream(values())
            .collect(Collectors.toMap(ErrorCodes::getMessage, errorCodes -> errorCodes));

    private static final Map<Integer, ErrorCodes> STATUS_CODES_ENTRIES = Arrays.stream(values())
            .collect(Collectors.toMap(
                    ErrorCodes::getStatusCode,
                    errorCodes -> errorCodes,
                    (old, current) -> current)
            );

    private final Integer statusCode;

    private final String message;

    public static Stream<ErrorCodes> findEntriesByMessages(Stream<String> messages) {
        return messages.map(message ->
                ErrorCodes.MESSAGES_ENTRIES.getOrDefault(message, ErrorCodes.UNKNOWN)
        );
    }

    public static Stream<ErrorCodes> findEntriesByStatusCodes(Stream<Integer> statusCodes) {
        return statusCodes.map(code ->
                ErrorCodes.STATUS_CODES_ENTRIES.getOrDefault(code, ErrorCodes.UNKNOWN)
        );
    }
}
