package doan.ptit.programmingtrainingcenter.repository;


import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass,String> {
    List<CourseClass> findAllByCourseId(String courseId);
}
