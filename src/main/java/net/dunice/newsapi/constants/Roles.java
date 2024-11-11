package net.dunice.newsapi.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Roles {
    USER("user");

    private final String roleName;

    public static Roles forRoleName(String roleName) {
        return Arrays.stream(values())
                .filter(roles -> roles.roleName.equals(roleName))
                .findFirst()
                .orElseThrow();
    }
}
