package net.dunice.newsapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;

@Entity
@Table(name = "logs")
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String requestMethod;

    private Integer responseCode;

    private String endpoint;

    private Boolean requireAuth;

    private Integer errorCodesStatus;

    private String errorCodesMessage;

    @Column
    @CreationTimestamp
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_uuid")
    private UserEntity user;
}
