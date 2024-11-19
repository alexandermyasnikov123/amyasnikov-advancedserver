package net.dunice.newsapi.entities.callbacks;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.services.FilesService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEntityCallbacks {
    private final FilesService service;

    private volatile String previousAvatar;

    @PostLoad
    public void cachePreviousState(UserEntity currentState) {
        previousAvatar = currentState.getAvatar();
    }

    @PostRemove
    public void deleteAvatarFile(UserEntity ignored) {
        service.deleteFileByUrl(previousAvatar);
    }

    @PostUpdate
    public void deleteAvatarIfAbsent(UserEntity currentState) {
        if (currentState.getAvatar().equals(previousAvatar)) {
            return;
        }
        deleteAvatarFile(currentState);
    }
}
