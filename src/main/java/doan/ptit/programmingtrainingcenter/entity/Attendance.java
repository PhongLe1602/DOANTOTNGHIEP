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
    @JoinColumn(name = "session_id", nullable = false)
    AttendanceSession session;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    User student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    Status status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @CreationTimestamp
    Date attendanceDate;

    public enum Status {
        PRESENT,
        ABSENT,
        LATE
    }
}
