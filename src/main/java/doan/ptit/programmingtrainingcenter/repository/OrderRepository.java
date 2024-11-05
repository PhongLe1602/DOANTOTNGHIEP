package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId);
}
