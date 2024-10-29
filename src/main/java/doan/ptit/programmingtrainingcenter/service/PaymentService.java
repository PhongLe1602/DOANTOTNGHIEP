package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.entity.Order;
import doan.ptit.programmingtrainingcenter.entity.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface PaymentService {
    Payment createPayment(Order order, BigDecimal amount, String paymentMethodId);
    Payment confirmPayment(String paymentId, boolean isSuccess);
    Payment getPaymentById(String paymentId);
}
