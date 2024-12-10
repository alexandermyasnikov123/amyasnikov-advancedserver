package net.dunice.features.auth.repositories;

import net.dunice.features.auth.entities.UserEntityDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserEntityDetails, UUID> {
    Optional<UserEntityDetails> findByUsername(String username);

    void deleteByUsername(String username);
}
