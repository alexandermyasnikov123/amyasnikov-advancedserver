package net.dunice.newsapi.entities;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.dunice.newsapi.entities.callbacks.ImageDeleteCallbacks;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "news_graph_join_all",
        includeAllAttributes = true
)
@EntityListeners(value = ImageDeleteCallbacks.class)
public class NewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String image;

    @ManyToMany
    @JoinTable(
            name = "news_with_tags",
            joinColumns = @JoinColumn(name = "news_id"),
            foreignKey = @ForeignKey(name = "nwt_news_fk"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            inverseForeignKey = @ForeignKey(name = "nwt_tags_fk")
    )
    private List<TagEntity> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_entity_id", referencedColumnName = "uuid")
    @ToString.Exclude
    @JsonUnwrapped
    private UserEntity author;
}
