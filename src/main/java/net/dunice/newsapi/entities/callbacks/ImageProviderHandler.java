package net.dunice.newsapi.entities.callbacks;

import lombok.RequiredArgsConstructor;
import net.dunice.newsapi.entities.ImageProvider;
import net.dunice.newsapi.services.FilesService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class ImageProviderHandler {
    private final FilesService service;

    private volatile String previousAvatar;

    public void cachePreviousState(ImageProvider currentState) {
        previousAvatar = currentState.getImageUrl();
    }

    public void deleteCachedAvatar() {
        service.deleteFileByUrl(previousAvatar);
    }

    public void deleteAvatarIfAbsent(ImageProvider currentState) {
        if (currentState.getImageUrl().equals(previousAvatar)) {
            return;
        }
        deleteCachedAvatar();
    }
}
