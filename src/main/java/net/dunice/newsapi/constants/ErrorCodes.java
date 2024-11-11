package net.dunice.newsapi.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCodes {
    UNKNOWN(0, ValidationConstants.UNKNOWN),

    SUCCESS(1, ValidationConstants.SUCCESS),

    USERNAME_SIZE_NOT_VALID(1, ValidationConstants.USERNAME_SIZE_NOT_VALID),

    ROLE_SIZE_NOT_VALID(2, ValidationConstants.ROLE_SIZE_NOT_VALID),

    EMAIL_SIZE_NOT_VALID(3, ValidationConstants.EMAIL_SIZE_NOT_VALID),

    MUST_NOT_BE_NULL(4, ValidationConstants.MUST_NOT_BE_NULL),

    USER_NOT_FOUND(5, ValidationConstants.USER_NOT_FOUND),

    TOKEN_NOT_PROVIDED(6, ValidationConstants.TOKEN_NOT_PROVIDED),

    UNAUTHORISED(7, ValidationConstants.UNAUTHORISED),

    USER_EMAIL_NOT_NULL(8, ValidationConstants.USER_EMAIL_NOT_NULL),

    USER_PASSWORD_NOT_VALID(9, ValidationConstants.USER_PASSWORD_NOT_VALID),

    USER_ROLE_NOT_NULL(10, ValidationConstants.USER_ROLE_NOT_NULL),

    NEWS_DESCRIPTION_SIZE(11, ValidationConstants.NEWS_DESCRIPTION_SIZE),

    NEWS_DESCRIPTION_NOT_NULL(12, ValidationConstants.NEWS_DESCRIPTION_NOT_NULL),

    NEWS_TITLE_SIZE(13, ValidationConstants.NEWS_TITLE_SIZE),

    NEWS_TITLE_NOT_NULL(14, ValidationConstants.NEWS_TITLE_NOT_NULL),

    USER_EMAIL_NOT_VALID(17, ValidationConstants.USER_EMAIL_NOT_VALID),

    PAGE_SIZE_NOT_VALID(18, ValidationConstants.PAGE_SIZE_NOT_VALID),

    PER_PAGE_MIN_NOT_VALID(19, ValidationConstants.PER_PAGE_MIN_NOT_VALID),

    PER_PAGE_MAX_NOT_VALID(19, ValidationConstants.PER_PAGE_MAX_NOT_VALID),

    CODE_NOT_NULL(20, ValidationConstants.CODE_NOT_NULL),

    EXCEPTION_HANDLER_NOT_PROVIDED(21, ValidationConstants.EXCEPTION_HANDLER_NOT_PROVIDED),

    REQUEST_IS_NOT_MULTIPART(22, ValidationConstants.REQUEST_IS_NOT_MULTIPART),

    MAX_UPLOAD_SIZE_EXCEEDED(23, ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED),

    USER_AVATAR_NOT_NULL(24, ValidationConstants.USER_AVATAR_NOT_NULL),

    PASSWORD_NOT_VALID(25, ValidationConstants.PASSWORD_NOT_VALID),

    PASSWORD_NOT_NULL(26, ValidationConstants.PASSWORD_NOT_NULL),

    NEWS_NOT_FOUND(27, ValidationConstants.NEWS_NOT_FOUND),

    ID_MUST_BE_POSITIVE(29, ValidationConstants.ID_MUST_BE_POSITIVE),

    USER_ALREADY_EXISTS(30, ValidationConstants.USER_ALREADY_EXISTS),

    TODO_TEXT_NOT_NULL(31, ValidationConstants.TODO_TEXT_NOT_NULL),

    TODO_TEXT_SIZE_NOT_VALID(32, ValidationConstants.TODO_TEXT_SIZE_NOT_VALID),

    TODO_STATUS_NOT_NULL(33, ValidationConstants.TODO_STATUS_NOT_NULL),

    TASK_NOT_FOUND(34, ValidationConstants.TASK_NOT_FOUND),

    TASK_PATCH_UPDATED_NOT_CORRECT_COUNT(35, ValidationConstants.TASK_PATCH_UPDATED_NOT_CORRECT_COUNT),

    TASKS_PAGE_GREATER_OR_EQUAL_1(37, ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1),

    TASKS_PER_PAGE_GREATER_OR_EQUAL_1(38, ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1),

    TASKS_PER_PAGE_LESS_OR_EQUAL_100(39, ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100),

    REQUIRED_INT_PARAM_PAGE_IS_NOT_PRESENT(40, ValidationConstants.REQUIRED_INT_PARAM_PAGE_IS_NOT_PRESENT),

    REQUIRED_INT_PARAM_PER_PAGE_IS_NOT_PRESENT(41, ValidationConstants.REQUIRED_INT_PARAM_PER_PAGE_IS_NOT_PRESENT),

    USER_NAME_HAS_TO_BE_PRESENT(43, ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT),

    TAGS_NOT_VALID(44, ValidationConstants.TAGS_NOT_VALID),

    NEWS_IMAGE_HAS_TO_BE_PRESENT(45, ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT),

    USER_WITH_THIS_EMAIL_ALREADY_EXIST(46, ValidationConstants.USER_WITH_THIS_EMAIL_ALREADY_EXIST),

    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(47, ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION),

    USERNAME_HAS_TO_BE_PRESENT(48, ValidationConstants.USERNAME_HAS_TO_BE_PRESENT),

    USER_AVATAR_NOT_VALID(49, ValidationConstants.USER_AVATAR_NOT_VALID),

    USER_ROLE_NOT_VALID(50, ValidationConstants.USER_ROLE_NOT_VALID),

    USER_PASSWORD_NULL(51, ValidationConstants.USER_PASSWORD_NULL),

    USER_ID_NULL(52, ValidationConstants.USER_ID_NULL),

    USERNAME_NULL(53, ValidationConstants.USERNAME_NULL),

    USER_ROLE_NULL(54, ValidationConstants.USER_ROLE_NULL),

    TOKEN_POSITION_MISMATCH(55, ValidationConstants.TOKEN_POSITION_MISMATCH),

    NEWS_IMAGE_LENGTH(56, ValidationConstants.NEWS_IMAGE_LENGTH),

    NEWS_ID_NULL(57, ValidationConstants.NEWS_ID_NULL);

    Integer statusCode;

    String message;
}
