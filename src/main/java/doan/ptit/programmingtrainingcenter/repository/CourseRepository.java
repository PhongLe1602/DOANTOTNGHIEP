package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> , JpaSpecificationExecutor<Course> {
    List<Course> findAllById(Iterable<String> ids);
}
