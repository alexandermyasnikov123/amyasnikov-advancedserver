package net.dunice.newsapi.repositories;

import net.dunice.newsapi.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findUserEntityByUsername(String username);

    Optional<UserEntity> findUserEntityByEmail(String email);
}
