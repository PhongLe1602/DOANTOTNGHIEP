package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> , JpaSpecificationExecutor<Payment> {
    Payment findByOrderId(String orderId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'COMPLETED' AND p.completedAt >= :fromDate AND p.completedAt < :toDate")
    BigDecimal calculateCompletedRevenueInRange(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);


    @Query("""
        SELECT oi.course.id AS courseId, c.title AS courseName, SUM(oi.price) AS totalRevenue
        FROM Payment p
        JOIN p.order o
        JOIN o.orderItems oi
        JOIN oi.course c
        WHERE p.status = 'COMPLETED' 
          AND p.completedAt >= :fromDate 
          AND p.completedAt <= :toDate
        GROUP BY oi.course.id, c.title
    """)
    List<Object[]> calculateRevenueByCourse(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED'")
    BigDecimal calculateTotalRevenue();


    @Query("SELECT MAX(p.transactionCode) FROM Payment p WHERE p.transactionCode LIKE 'TTT%'")
    String findMaxTransactionCode();

}
