package net.dunice.newsapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class RoleEntity {
    @Id
    @NonNull
    private String name;

    @OneToMany
    private List<UserEntity> users;
}
