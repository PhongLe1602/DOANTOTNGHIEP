package doan.ptit.programmingtrainingcenter.repository;


import doan.ptit.programmingtrainingcenter.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, String> {
    @Query("SELECT a FROM Assignment a " +
            "JOIN a.courseClass c " +
            "JOIN ClassStudent cs ON cs.courseClass = c " +
            "WHERE cs.student.id = :studentId")
    List<Assignment> findAssignmentsByStudentId(@Param("studentId") String studentId);

}
