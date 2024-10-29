package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.OrderRequest;
import doan.ptit.programmingtrainingcenter.entity.*;
import doan.ptit.programmingtrainingcenter.mapper.OrderMapper;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PaymentServiceImpl paymentService;

    @Override
    public Order addOrder(OrderRequest orderRequest) {
        User user = getUser(orderRequest.getUserId());
        PaymentMethod paymentMethod = getPaymentMethod(orderRequest.getPaymentMethodId());

        Order order = createOrder(orderRequest, user, paymentMethod);
        List<OrderItem> orderItems = createOrderItems(orderRequest, order);

        setOrderTotalAmount(order, orderItems);
        orderRepository.save(order);

        createEnrollments(user, orderItems);

        paymentService.createPayment(order, order.getTotalAmount(), paymentMethod.getId());
        return order;
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    // Helper method to get User entity
    private User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    // Helper method to get PaymentMethod entity
    private PaymentMethod getPaymentMethod(String paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("PaymentMethod Not Found"));
    }

    // Helper method to create Order entity
    private Order createOrder(OrderRequest orderRequest, User user, PaymentMethod paymentMethod) {
        return orderMapper.toEntity(orderRequest, user, paymentMethod);
    }

    // Helper method to create OrderItems from OrderRequest
    private List<OrderItem> createOrderItems(OrderRequest orderRequest, Order order) {
        List<Course> courses = courseRepository.findAllById(
                orderRequest.getItems().stream()
                        .map(OrderRequest.OrderItemRequest::getCourseId)
                        .collect(Collectors.toList())
        );
        return orderMapper.toOrderItemEntities(orderRequest.getItems(), order, courses);
    }

    // Helper method to calculate and set total amount for an Order
    private void setOrderTotalAmount(Order order, List<OrderItem> orderItems) {
        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
    }

    // Helper method to create Enrollments for each OrderItem
    private void createEnrollments(User user, List<OrderItem> orderItems) {
        orderItems.forEach(orderItem -> {
            Enrollment enrollment = Enrollment.builder()
                    .user(user)
                    .course(orderItem.getCourse())
                    .orderItem(orderItem)
                    .status(Enrollment.Status.PENDING)
                    .progress(BigDecimal.ZERO)
                    .enrollmentDate(new Date())
                    .build();
            enrollmentRepository.save(enrollment);
        });
    }



}
