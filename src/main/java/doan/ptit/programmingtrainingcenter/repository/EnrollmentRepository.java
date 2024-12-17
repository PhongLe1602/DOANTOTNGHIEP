package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.Order;
import doan.ptit.programmingtrainingcenter.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepository  extends JpaRepository<Enrollment, String> {
    List<Enrollment> findByUserId(String useId);

    List<Enrollment> findByOrderItem(OrderItem orderItem);

    Enrollment findByUserIdAndCourseId(String userId, String courseId);

    @Query("""
        SELECT e FROM Enrollment e
        JOIN FETCH e.user u
        JOIN FETCH e.course c
        WHERE e.status = 'ACTIVE' ORDER BY e.enrollmentDate DESC
    """)
    List<Enrollment> findTop3NewestEnrollments(Pageable pageable);

}
