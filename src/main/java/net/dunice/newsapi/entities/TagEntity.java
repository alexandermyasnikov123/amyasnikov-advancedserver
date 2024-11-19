package net.dunice.newsapi.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String title;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "news_with_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            foreignKey = @ForeignKey(name = "nwt_tags_fk"),
            inverseJoinColumns = @JoinColumn(name = "news_id"),
            inverseForeignKey = @ForeignKey(name = "nwt_news_fk")
    )
    List<NewsEntity> news;
}
