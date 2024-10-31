package doan.ptit.programmingtrainingcenter.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "lessons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "section_id", foreignKey = @ForeignKey(name = "FK_section_id"), nullable = false)
    @JsonBackReference
    Section section;

    @Column(nullable = false, length = 255)
    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    @Column(nullable = false)
    String videoLink;

    @Column(nullable = false)
    int duration; // Thời gian bài học (phút)

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;
}
