package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "class_students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    CourseClass courseClass;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    User student;

    @Column(name = "joined_date", nullable = false)
    @Temporal(TemporalType.DATE)
    Date joinedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15, columnDefinition = "ENUM('STUDYING', 'COMPLETED', 'DROPPED') DEFAULT 'STUDYING'")
    Status status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    Date createdAt;

    public enum Status {
        STUDYING,
        COMPLETED,
        DROPPED
    }
}
