package net.dunice.newsapi.entities.callbacks;

import net.dunice.newsapi.entities.UserEntity;
import net.dunice.newsapi.services.FilesService;
import org.springframework.stereotype.Component;

@Component
public class UserEntityCallbacks extends BaseImageProviderCallbacks<UserEntity> {

    public UserEntityCallbacks(FilesService service) {
        super(service);
    }
}
