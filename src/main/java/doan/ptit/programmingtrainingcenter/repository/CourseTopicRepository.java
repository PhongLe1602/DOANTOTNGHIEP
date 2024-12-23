package doan.ptit.programmingtrainingcenter.repository;


import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.CourseTopic;
import doan.ptit.programmingtrainingcenter.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseTopicRepository extends JpaRepository<CourseTopic, String> {
    Optional<CourseTopic> findByCourseIdAndTopicId(String courseId, String topicId);

    List<CourseTopic> findByTopicId(String topicId);
    void deleteByTopicId(String topicId);
}
