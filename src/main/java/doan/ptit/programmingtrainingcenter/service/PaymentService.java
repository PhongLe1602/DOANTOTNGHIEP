package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.entity.Order;
import doan.ptit.programmingtrainingcenter.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public interface PaymentService {
    Payment createPayment(Order order, BigDecimal amount, String paymentMethodId);
    Payment confirmPayment(String paymentId, boolean isSuccess);
    Payment getPaymentById(String paymentId);
    boolean verifyVNPayPayment(Map<String, String> params);
    List<Payment> getAllPayments();
    Page<Payment> getPaymentsWithFilters(String status, String orderId, Date fromDate, Date toDate, Pageable pageable);
}
