package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "course_classes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseClass {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, length = 100)
    String name;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    User instructor;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    Date endDate;

    @Column(name = "study_time", nullable = false, length = 50)
    String studyTime;

    @Column(name = "study_days", nullable = false, length = 50)
    String studyDays;

    @Column(name = "current_student_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    int currentStudentCount;


    @Column(name = "max_students", columnDefinition = "INT DEFAULT 30")
    int maxStudents;

    @Column(name = "completed_sessions", nullable = false, columnDefinition = "INT DEFAULT 0")
    int completedSessions;

    @Column(name = "total_sessions", nullable = false)
    int totalSessions;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15, columnDefinition = "ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'ACTIVE'")
    Status status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    Date createdAt;

    public enum Status {
        ACTIVE,
        COMPLETED,
        CANCELLED
    }

}
