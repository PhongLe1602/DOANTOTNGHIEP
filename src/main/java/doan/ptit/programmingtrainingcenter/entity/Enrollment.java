package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "enrollments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_enrollment_user_id"), nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "FK_enrollment_course_id"), nullable = false)
    Course course;

    @ManyToOne
    @JoinColumn(name = "order_item_id", foreignKey = @ForeignKey(name = "FK_enrollment_order_item_id"))
    OrderItem orderItem;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enrollment_date", nullable = false)
    Date enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    Status status = Status.PENDING;
    @Column(nullable = false, precision = 5, scale = 2)
    BigDecimal progress = BigDecimal.valueOf(0.00); // Tiến độ hoàn thành của khóa học (%)

    @Temporal(TemporalType.TIMESTAMP)
    Date lastAccessed; // Thời gian học viên lần cuối truy cập khóa học

    public enum Status {
        INACTIVE,
        ACTIVE,
        COMPLETED,
        CANCELED,
        PENDING,
        STUDYING
    }
}
