package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> , JpaSpecificationExecutor<Course> {
    List<Course> findAllById(Iterable<String> ids);

    @Query(value = "SELECT c.* FROM courses c " +
            "JOIN course_instructors ci ON c.id = ci.course_id " +
            "WHERE ci.instructor_id = :instructorId",
            nativeQuery = true)
    List<Course> findCoursesByInstructorId(@Param("instructorId") String instructorId);


    List<Course> findByTitleContainingOrDescriptionContaining(String title, String description);

}
