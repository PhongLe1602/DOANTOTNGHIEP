package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.Order;
import doan.ptit.programmingtrainingcenter.entity.OrderItem;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository  extends JpaRepository<Enrollment, String> , JpaSpecificationExecutor<Enrollment> {
    List<Enrollment> findByUserId(String useId);

    List<Enrollment> findByOrderItem(OrderItem orderItem);

    Enrollment findByUserIdAndCourseId(String userId, String courseId);

    @Query("""
        SELECT e FROM Enrollment e
        JOIN FETCH e.user u
        JOIN FETCH e.course c
        WHERE e.status = 'ACTIVE' OR e.status = 'STUDYING' ORDER BY e.enrollmentDate DESC
    """)
    List<Enrollment> findTop3NewestEnrollments(Pageable pageable);


    // Get distinct users with enrollments
    @Query("SELECT DISTINCT e.user FROM Enrollment e")
    List<User> findDistinctUsersWithEnrollments();

    // Get all enrollments for a specific user
    @Query("""
        SELECT e FROM Enrollment e
        JOIN FETCH e.user
        JOIN FETCH e.course
        LEFT JOIN FETCH e.orderItem
        WHERE e.user.id = :userId
    """)
    List<Enrollment> findEnrollmentDetailsByUserId(@Param("userId") String userId);

    @Query("SELECT COUNT(DISTINCT e.user.id) FROM Enrollment e")
    long countDistinctUserIdFromEnrollments();

    long countByStatus(Enrollment.Status status);



}
