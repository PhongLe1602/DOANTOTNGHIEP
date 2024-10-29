package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.Order;
import doan.ptit.programmingtrainingcenter.entity.OrderItem;
import doan.ptit.programmingtrainingcenter.entity.Payment;
import doan.ptit.programmingtrainingcenter.repository.EnrollmentRepository;
import doan.ptit.programmingtrainingcenter.repository.OrderRepository;
import doan.ptit.programmingtrainingcenter.repository.PaymentMethodRepository;
import doan.ptit.programmingtrainingcenter.repository.PaymentRepository;
import doan.ptit.programmingtrainingcenter.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Payment createPayment(Order order, BigDecimal amount, String paymentMethodId) {
        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .paymentMethod(paymentMethodRepository.findById(paymentMethodId)
                        .orElseThrow(() -> new RuntimeException("Payment Method Not Found")))
                .status(Payment.PaymentStatus.valueOf("PENDING"))
                .createdAt(new Date())
                .build();

        return paymentRepository.save(payment);
    }
    @Override
    public Payment confirmPayment(String paymentId, boolean isSuccess) {
        Payment.PaymentStatus newStatus = isSuccess ? Payment.PaymentStatus.COMPLETED : Payment.PaymentStatus.FAILED;
        Payment payment = updatePaymentStatus(paymentId, newStatus);
        updateOrderStatus(payment.getOrder(), newStatus);
        // Kích hoạt enrollments nếu thanh toán thành công
        if (newStatus == Payment.PaymentStatus.COMPLETED) {
            activateEnrollments(payment);
        }
        return payment;
    }

    @Override
    public Payment getPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment  Not Found"));
    }

    public Payment updatePaymentStatus(String paymentId, Payment.PaymentStatus newStatus) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment Not Found"));

        payment.setStatus(newStatus);

        if (newStatus == Payment.PaymentStatus.COMPLETED || newStatus == Payment.PaymentStatus.FAILED) {
            payment.setCompletedAt(new Date());
        }

        return paymentRepository.save(payment);
    }



    private void activateEnrollments(Payment payment) {
        List<OrderItem> orderItems = payment.getOrder().getOrderItems();
        for (OrderItem orderItem : orderItems) {
            List<Enrollment> enrollments = enrollmentRepository.findByOrderItem(orderItem);
            enrollments.forEach(enrollment -> {
                enrollment.setStatus(Enrollment.Status.ACTIVE);
                enrollmentRepository.save(enrollment);
            });
        }
    }

    private void updateOrderStatus(Order order, Payment.PaymentStatus paymentStatus) {
        if (paymentStatus == Payment.PaymentStatus.COMPLETED) {
            order.setStatus(Order.OrderStatus.COMPLETED); // Cập nhật trạng thái đơn hàng thành COMPLETED
        } else if (paymentStatus == Payment.PaymentStatus.FAILED) {
            order.setStatus(Order.OrderStatus.FAILED); // Cập nhật trạng thái đơn hàng thành FAILED
        }

        // Lưu lại đơn hàng đã cập nhật
        orderRepository.save(order);
    }



}
