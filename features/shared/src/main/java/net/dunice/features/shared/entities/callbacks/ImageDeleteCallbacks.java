package net.dunice.features.shared.entities.callbacks;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import net.dunice.features.files_api.services.FilesService;
import net.dunice.features.shared.entities.NewsEntity;
import net.dunice.features.shared.entities.UserEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class ImageDeleteCallbacks {
    private final FilesService service;

    private volatile String previousAvatar;

    @PostLoad
    public void cachePreviousState(Object currentState) {
        previousAvatar = extractImageUrl(currentState);
    }

    @PostRemove
    public void deleteCachedAvatar(Object ignored) {
        service.deleteFileByUrl(previousAvatar);
    }

    @PostUpdate
    public void deleteAvatarIfAbsent(Object currentState) {
        if (extractImageUrl(currentState).equals(previousAvatar)) {
            return;
        }
        deleteCachedAvatar(currentState);
    }

    private String extractImageUrl(Object object) {
        return switch (object) {
            case UserEntity user -> user.getAvatar();
            case NewsEntity news -> news.getImage();
            default -> throw new IllegalArgumentException("An unsupported argument was provided.");
        };
    }
}
