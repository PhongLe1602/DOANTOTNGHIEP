package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository  extends JpaRepository<Enrollment, String> {
    List<Enrollment> findByUserId(String useId);
}
