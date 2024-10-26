package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "course_instructors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseInstructor {

    @Id
    @Column(length = 36)
    String id;  // Khóa chính, ID của bản ghi

    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "FK_instructor_course_id"), nullable = false)
    Course course;  // Tham chiếu đến thực thể Course

    @ManyToOne
    @JoinColumn(name = "instructor_id", foreignKey = @ForeignKey(name = "FK_user_instructor_id"), nullable = false)
    User instructor;  // Tham chiếu đến thực thể User
}
