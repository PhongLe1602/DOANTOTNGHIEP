package doan.ptit.programmingtrainingcenter.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    Notification notification;

    @ManyToOne
    @JoinColumn(name = "recipient_id", foreignKey = @ForeignKey(name = "FK_notification_user_id"), nullable = false)
    User recipient;

    @Column(nullable = false)
    boolean isRead;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date readAt;
}
