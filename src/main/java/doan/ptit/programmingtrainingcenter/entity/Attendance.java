package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "attendance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    CourseClass classEntity;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    User student;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    Date date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    Status status;

    @Column(columnDefinition = "TEXT")
    String note;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    User createdBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    Date createdAt;

    public enum Status {
        PRESENT,
        ABSENT,
        LATE
    }
}
