package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, length = 255)
    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(nullable = false)
    int duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    Level level;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @Column(length = 255)
    String thumbnail;

    @Column(name = "student_count", columnDefinition = "INT DEFAULT 0")
    int studentCount;

    @ManyToOne
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "FK_category_id"))
    Category category;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

    @ManyToMany
    @JoinTable(
            name = "course_instructors",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id")
    )
    List<User> instructors; // Danh sách giảng viên của khóa học
    public enum Level {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }
}
