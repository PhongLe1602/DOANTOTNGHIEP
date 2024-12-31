package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.ClassStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudent, String> {

    Optional<ClassStudent> findByCourseClassIdAndStudentId(String courseClassId, String studentId);

    List<ClassStudent> findByStudentIdAndStatus(String studentId, ClassStudent.Status status);
}
