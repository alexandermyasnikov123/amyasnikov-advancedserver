package net.dunice.newsapi.entities.callbacks;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import net.dunice.newsapi.entities.ImageProvider;
import net.dunice.newsapi.services.FilesService;
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
    public void cachePreviousState(ImageProvider currentState) {
        previousAvatar = currentState.getImageUrl();
    }

    @PostRemove
    public void deleteCachedAvatar(ImageProvider ignored) {
        service.deleteFileByUrl(previousAvatar);
    }

    @PostUpdate
    public void deleteAvatarIfAbsent(ImageProvider currentState) {
        if (currentState.getImageUrl().equals(previousAvatar)) {
            return;
        }
        deleteCachedAvatar(currentState);
    }
}
