package doan.ptit.programmingtrainingcenter.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "course_class_id", nullable = false)
    CourseClass courseClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    SessionType sessionType;

    @Temporal(TemporalType.TIMESTAMP)
    Date sessionDate;

    @Temporal(TemporalType.TIMESTAMP)
    Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    Date endTime;

    Integer duration;

    @Column(columnDefinition = "TEXT")
    String description;


    // Thêm link học online
    @Column(name = "online_link", length = 255)
    String onlineLink;

    // Thêm trường location
    @Column(name = "location", length = 255)
    String location;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

    public enum SessionType {
        ONLINE, OFFLINE, VIDEO
    }
}
