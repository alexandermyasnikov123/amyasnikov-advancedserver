package net.dunice.newsapi.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsEntity {
    @Id
    @GeneratedValue
    Integer id;

    String title;

    String description;

    String image;

    @ManyToMany(cascade = CascadeType.PERSIST)
    List<TagEntity> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity user;
}
