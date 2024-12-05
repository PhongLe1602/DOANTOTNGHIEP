package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "notification_recipients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false, foreignKey = @ForeignKey(name = "FK_notification_id"))
    Notification notification;

    @Column(nullable = false)
    String recipientId;

    @Column(nullable = false)
    boolean isRead;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date readAt;
}
