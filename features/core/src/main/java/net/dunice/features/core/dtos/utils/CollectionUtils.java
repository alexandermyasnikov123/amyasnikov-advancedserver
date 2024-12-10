package net.dunice.features.core.dtos.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtils {

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
