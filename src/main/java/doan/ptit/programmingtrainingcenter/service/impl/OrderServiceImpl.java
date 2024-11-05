package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.OrderCheckOutRequest;
import doan.ptit.programmingtrainingcenter.dto.request.OrderRequest;
import doan.ptit.programmingtrainingcenter.entity.*;
import doan.ptit.programmingtrainingcenter.mapper.OrderMapper;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private CartRepository cartRepository;

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

    @Override
    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order Not Found"));
    }

    @Transactional
    public Order checkout(String userId,OrderCheckOutRequest orderCheckOutRequest) {
        // Lấy giỏ hàng của người dùng
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        PaymentMethod paymentMethod = getPaymentMethod(orderCheckOutRequest.getPaymentMethodId());

        // Tạo Order mới từ thông tin trong Cart
        Order order = Order.builder()
                .user(cart.getUser())
                .totalAmount(cart.getTotalAmount())
                .status(Order.OrderStatus.PENDING)
                .paymentMethod(paymentMethod)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .build();
        // Chuyển từng CartItem thành OrderItem và thêm vào Order
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .course(cartItem.getCourse())
                        .price(cartItem.getPrice())
                        .build())
                .toList();

        // Gán danh sách OrderItem vào Order
        order.setOrderItems(orderItems);

        // Lưu Order
        Order savedOrder = orderRepository.save(order);

        // Xóa giỏ hàng sau khi đã tạo Order
        cartRepository.delete(cart);

        return savedOrder;

    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
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
