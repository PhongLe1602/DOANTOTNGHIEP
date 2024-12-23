package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "consults")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Consult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, length = 255)
    String fullName;

    @Column(nullable = false, length = 15)
    String phoneNumber;

    @Column(nullable = false, length = 255)
    String email;

    @Column(columnDefinition = "TEXT")
    String requestMessage;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15, columnDefinition = "VARCHAR(15) DEFAULT 'PENDING'")
    Status status;

    @ManyToOne
    @JoinColumn(name = "consultant_id", foreignKey = @ForeignKey(name = "FK_consultant_id"))
    User consultant;

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }
}
