package doan.ptit.programmingtrainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "courses_topic")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "FK_course_id"), nullable = false)
    Course course;

    @ManyToOne
    @JoinColumn(name = "topic_id", foreignKey = @ForeignKey(name = "FK_topic_id"), nullable = false)
    Topic topic;
}
