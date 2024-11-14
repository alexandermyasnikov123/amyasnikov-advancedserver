package net.dunice.newsapi.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtFieldsConstants {
    String username;

    String role;

    String uuid;
}
