package net.dunice.newsapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {
    @Id
    private String name;

    @OneToMany(mappedBy = "role")
    private List<UserEntity> users;
}
