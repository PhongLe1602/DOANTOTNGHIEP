package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.entity.*;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.PaymentService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


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

    @Autowired
    private CourseRepository courseRepository;

    public Payment createPayment(Order order, BigDecimal amount, String paymentMethodId) {
        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .paymentMethod(paymentMethodRepository.findById(paymentMethodId)
                        .orElseThrow(() -> new RuntimeException("Payment Method Not Found")))
                .status(Payment.PaymentStatus.valueOf("PENDING"))
                .createdAt(new Date())
                .build();
//        order.setStatus(Order.OrderStatus.COMPLETED);
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
    @Override
    public boolean verifyVNPayPayment(Map<String, String> params) {
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TxnRef = params.get("vnp_TxnRef");
        String vnp_TransactionNo = params.get("vnp_TransactionNo");



        boolean isSuccess = "00".equals(vnp_ResponseCode);
        Payment.PaymentStatus newStatus = isSuccess ? Payment.PaymentStatus.COMPLETED : Payment.PaymentStatus.FAILED;


        Payment payment = updatePaymentStatusVNPay(vnp_TxnRef, newStatus ,vnp_TransactionNo );


        if (isSuccess) {
            activateEnrollments(payment);
        }

        return isSuccess;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Page<Payment> getPaymentsWithFilters(String status, String orderId, String customerName,
                                                Date fromDate, Date toDate, Pageable pageable) {
        Specification<Payment> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join with Order and User entities
            Join<Payment, Order> orderJoin = root.join("order");
            Join<Order, User> userJoin = orderJoin.join("user");

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"),
                        Payment.PaymentStatus.valueOf(status)));
            }

            if (orderId != null && !orderId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(orderJoin.get("id"), orderId));
            }

            if (customerName != null && !customerName.isEmpty()) {
                String searchTerm = "%" + customerName.toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(userJoin.get("fullName")), searchTerm));
            }

            if (fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"), fromDate));
            }

            if (toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"), toDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return paymentRepository.findAll(specification, pageable);
    }


    public Payment updatePaymentStatus(String paymentId, Payment.PaymentStatus newStatus) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment  Not Found"));

        Order order = payment.getOrder();

        order.setStatus(Order.OrderStatus.COMPLETED);

        orderRepository.save(order);

        payment.setStatus(newStatus);

        String prefix = "TT";
        if ("QRCODE".equals(payment.getPaymentMethod().getCode())) {
            prefix = "CK";
        }

        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String random = String.format("%04d", new Random().nextInt(10000));
        String transactionCode = prefix + timestamp + random;
        payment.setTransactionCode(transactionCode);

        if (newStatus == Payment.PaymentStatus.COMPLETED || newStatus == Payment.PaymentStatus.FAILED) {
            payment.setCompletedAt(new Date());
        }

        return paymentRepository.save(payment);
    }

    public Payment updatePaymentStatusVNPay(String orderId, Payment.PaymentStatus newStatus ,String transactionCode) {
        Payment payment = paymentRepository.findByOrderId(orderId);

        if(payment.getTransactionCode() == null) {
            payment.setTransactionCode(transactionCode);
        }

        payment.setStatus(newStatus);
        updateOrderStatus(payment.getOrder(), newStatus);

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
                Category.CategoryType categoryType = enrollment.getCourse().getCategory().getType();
                // Cập nhật trạng thái Enrollment
                if (categoryType == Category.CategoryType.VIDEO) {
                    enrollment.setStatus(Enrollment.Status.STUDYING);
                } else if (categoryType == Category.CategoryType.ONLINE || categoryType == Category.CategoryType.OFFLINE) {
                    enrollment.setStatus(Enrollment.Status.ACTIVE);
                }
                enrollmentRepository.save(enrollment);

                // Tăng số lượng học viên của khóa học
                Course course = orderItem.getCourse();
                course.setStudentCount(course.getStudentCount() + 1);
                courseRepository.save(course); // Lưu cập nhật vào DB
            });
        }
    }


    private void updateOrderStatus(Order order, Payment.PaymentStatus paymentStatus) {
        if (paymentStatus == Payment.PaymentStatus.COMPLETED) {
            order.setStatus(Order.OrderStatus.COMPLETED); // Cập nhật trạng thái đơn hàng thành COMPLETED
            order.setPaymentStatus(Order.PaymentStatus.COMPLETED);
        } else if (paymentStatus == Payment.PaymentStatus.FAILED) {
            order.setStatus(Order.OrderStatus.FAILED); // Cập nhật trạng thái đơn hàng thành FAILED
            order.setPaymentStatus(Order.PaymentStatus.FAILED);
        }

        // Lưu lại đơn hàng đã cập nhật
        orderRepository.save(order);
    }



}
