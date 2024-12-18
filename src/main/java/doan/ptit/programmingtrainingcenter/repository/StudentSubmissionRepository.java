package doan.ptit.programmingtrainingcenter.repository;


import doan.ptit.programmingtrainingcenter.entity.StudentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentSubmissionRepository extends JpaRepository<StudentSubmission, String> {
    List<StudentSubmission> findByAssignmentId(String assignmentId);

    StudentSubmission findByAssignmentIdAndStudentId(String assignmentId, String studentId);
}
