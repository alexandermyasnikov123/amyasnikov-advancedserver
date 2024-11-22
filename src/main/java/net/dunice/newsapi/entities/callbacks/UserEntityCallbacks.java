package net.dunice.newsapi.entities.callbacks;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import net.dunice.newsapi.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserEntityCallbacks {
    private final ImageProviderHandler callbacks;

    @PostLoad
    public void postLoad(UserEntity entity) {
        callbacks.cachePreviousState(entity);
    }

    @PostRemove
    public void postRemove(UserEntity ignored) {
        callbacks.deleteCachedAvatar();
    }

    @PostUpdate
    public void postUpdate(UserEntity currentState) {
        callbacks.deleteAvatarIfAbsent(currentState);
    }
}
