package doan.ptit.programmingtrainingcenter.repository;


import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass,String> {
    List<CourseClass> findAllByCourseId(String courseId);

    @Query("SELECT cs.student FROM ClassStudent cs WHERE cs.courseClass.id = :classId")
    List<User> findUsersByClassId(@Param("classId") String classId);

    List<CourseClass> findByInstructorId(@Param("instructorId") String instructorId);
}
