package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.Order;
import doan.ptit.programmingtrainingcenter.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository  extends JpaRepository<Enrollment, String> {
    List<Enrollment> findByUserId(String useId);

    List<Enrollment> findByOrderItem(OrderItem orderItem);

    Enrollment findByUserIdAndCourseId(String userId, String courseId);

}
