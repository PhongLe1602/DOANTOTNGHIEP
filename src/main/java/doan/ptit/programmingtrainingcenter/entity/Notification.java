package doan.ptit.programmingtrainingcenter.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, length = 255)
    String title;

    @Column(columnDefinition = "TEXT")
    String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    Status status;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    @JsonManagedReference
    User creator;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<NotificationRecipient> recipients;

    public enum Type {
        SYSTEM,
        COURSE,
        USER
    }

    public enum Status {
        ACTIVE,
        ARCHIVED
    }


}
