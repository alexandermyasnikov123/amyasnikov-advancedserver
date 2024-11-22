package net.dunice.newsapi.entities.callbacks;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import net.dunice.newsapi.entities.ImageProvider;
import net.dunice.newsapi.services.FilesService;

@RequiredArgsConstructor
public class BaseImageProviderCallbacks<T extends ImageProvider> {
    private final FilesService service;

    private volatile String previousAvatar;

    @PostLoad
    public void cachePreviousState(T currentState) {
        previousAvatar = currentState.getImageUrl();
    }

    @PostRemove
    public void deleteAvatarFile(T ignored) {
        service.deleteFileByUrl(previousAvatar);
    }

    @PostUpdate
    public void deleteAvatarIfAbsent(T currentState) {
        if (currentState.getImageUrl().equals(previousAvatar)) {
            return;
        }
        deleteAvatarFile(currentState);
    }
}
