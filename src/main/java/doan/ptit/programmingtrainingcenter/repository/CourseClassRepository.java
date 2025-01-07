package doan.ptit.programmingtrainingcenter.repository;


import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass,String> , JpaSpecificationExecutor<CourseClass> {
    List<CourseClass> findAllByCourseId(String courseId);

    List<CourseClass> findAllByCourseIdAndStatus(String courseId, CourseClass.Status status);

    @Query("SELECT cs.student FROM ClassStudent cs WHERE cs.courseClass.id = :classId")
    List<User> findUsersByClassId(@Param("classId") String classId);

    List<CourseClass> findByInstructorId(@Param("instructorId") String instructorId);

    @Query("SELECT COUNT(cc) FROM CourseClass cc " +
            "WHERE cc.instructor.id = :instructorId AND cc.status = :status")
    long countByInstructorIdAndStatus(
            @Param("instructorId") String instructorId,
            @Param("status") CourseClass.Status status
    );


    @Query("SELECT cs.courseClass FROM ClassStudent cs WHERE cs.student.id = :studentId")
    List<CourseClass> findClassesByStudentId(@Param("studentId") String studentId);


}
