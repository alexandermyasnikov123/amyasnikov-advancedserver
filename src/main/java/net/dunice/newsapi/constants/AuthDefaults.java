package net.dunice.newsapi.constants;

public interface AuthDefaults {
    String[] FULL_PERMITTED_ENDPOINTS = {"auth/**", "file/**"};

    String[] PERMITTED_GET_ENDPOINTS = {"news/**"};
}
