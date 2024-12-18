package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "student_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    User student;

    @Column(name = "file_url", nullable = false, length = 255)
    String fileUrl;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submission_date", updatable = false)
    Date submissionDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, columnDefinition = "ENUM('PENDING', 'GRADED') DEFAULT 'PENDING'")
    Status status;

    @Column(name = "score", precision = 5, scale = 2)
    BigDecimal score;

    @Column(columnDefinition = "TEXT")
    String feedback;

    public enum Status {
        PENDING,
        GRADED
    }
}
