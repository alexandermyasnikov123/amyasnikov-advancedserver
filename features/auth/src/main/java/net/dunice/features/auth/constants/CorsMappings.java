package net.dunice.features.auth.constants;

public interface CorsMappings {
    String[] FULL_PERMITTED_ENDPOINTS = {"auth/**", "file/**"};

    String[] PERMITTED_GET_ENDPOINTS = {"news/**"};

    String[] PERMITTED_POST_ENDPOINT = {"user/**"};
}
