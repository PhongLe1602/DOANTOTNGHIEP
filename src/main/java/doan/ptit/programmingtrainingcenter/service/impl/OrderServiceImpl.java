package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.configuration.VNPayConfig;
import doan.ptit.programmingtrainingcenter.dto.request.OrderCheckOutNowRequest;
import doan.ptit.programmingtrainingcenter.dto.request.OrderCheckOutRequest;
import doan.ptit.programmingtrainingcenter.dto.request.OrderRequest;
import doan.ptit.programmingtrainingcenter.dto.response.OrderResponse;
import doan.ptit.programmingtrainingcenter.dto.response.PagedResponse;
import doan.ptit.programmingtrainingcenter.entity.*;
import doan.ptit.programmingtrainingcenter.mapper.OrderMapper;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.OrderService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private VNPayConfig VNPayConfig;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    @Transactional
    public OrderResponse addOrder(OrderRequest orderRequest) {
        User user = getUser(orderRequest.getUserId());
        PaymentMethod paymentMethod = getPaymentMethod(orderRequest.getPaymentMethodId());

        // Tạo Order và các OrderItem
        Order order = createOrder(orderRequest, user, paymentMethod);
        List<OrderItem> orderItems = createOrderItems(orderRequest, order);

        // Tính tổng số tiền và lưu Order
        setOrderTotalAmount(order, orderItems);
        Order saveOrdered = orderRepository.save(order);

        // Tạo Payment cho Order
        Payment payment = createPayment(saveOrdered, paymentMethod);
        createEnrollments(user, orderItems);
        paymentService.createPayment(saveOrdered, saveOrdered.getTotalAmount(), paymentMethod.getId());
        // Nếu phương thức thanh toán là VNPay
        if ("VNPAY".equalsIgnoreCase(paymentMethod.getCode())) {
            // Tạo đường dẫn thanh toán VNPay
            String paymentUrl = generateVnpayPaymentUrl(saveOrdered, request);

            // Trả về OrderResponse với paymentUrl
            return OrderResponse.builder()
                    .order(saveOrdered)
                    .paymentUrl(paymentUrl)
                    .build();
        }

        // Nếu thanh toán trực tiếp
        payment.setStatus(Payment.PaymentStatus.COMPLETED); // Cập nhật trạng thái Payment
        saveOrdered.setStatus(Order.OrderStatus.COMPLETED);
        paymentRepository.save(payment);
        orderRepository.save(saveOrdered);


        // Tạo Enrollment với trạng thái ACTIVE
        createEnrollmentsWithActiveStatus(user, orderItems);

        for (OrderItem orderItem : orderItems) {
            Course course = orderItem.getCourse();
            course.setStudentCount(course.getStudentCount() + 1);
            courseRepository.save(course);
        }
        // Trả về thông tin Order
        return OrderResponse.builder()
                .order(saveOrdered)
                .build();
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
    public OrderResponse checkout(String userId,OrderCheckOutRequest orderCheckOutRequest) {
        // Lấy giỏ hàng của người dùng
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        PaymentMethod paymentMethod = getPaymentMethod(orderCheckOutRequest.getPaymentMethodId());

        // Tạo Order mới từ thông tin trong Cart
        Order order = Order.builder()
                .user(cart.getUser())
                .totalAmount(cart.getTotalAmount())
                .status(Order.OrderStatus.PENDING)
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
        createEnrollments(cart.getUser(), orderItems);
        paymentService.createPayment(savedOrder, savedOrder.getTotalAmount(), paymentMethod.getId());
        // Xóa giỏ hàng sau khi đã tạo Order
        cartRepository.delete(cart);
        if ("VNPAY".equalsIgnoreCase(paymentMethod.getCode())) {
            String paymentUrl = generateVnpayPaymentUrl(savedOrder,request);
            return OrderResponse.builder()
                    .order(savedOrder)
                    .paymentUrl(paymentUrl)
                    .build();
        }
        for (OrderItem orderItem : orderItems) {
            Course course = orderItem.getCourse();
            course.setStudentCount(course.getStudentCount() + 1);
            courseRepository.save(course);
        }

        return OrderResponse.builder()
                .order(savedOrder)
                .build();

    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public OrderResponse checkoutNow(String userId, OrderCheckOutNowRequest orderCheckOutNowRequest) {
        // Lấy thông tin người dùng
        User user = getUser(userId);

        // Lấy thông tin phương thức thanh toán
        PaymentMethod paymentMethod = getPaymentMethod(orderCheckOutNowRequest.getPaymentMethodId());

        // Lấy thông tin khóa học
        Course course = courseRepository.findById(orderCheckOutNowRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course Not Found"));

        // Tạo đối tượng Order
        Order order = Order.builder()
                .user(user)
                .totalAmount(course.getPrice())
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .build();

        // Tạo OrderItem từ thông tin khóa học
        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .course(course)
                .price(course.getPrice())
                .build();

        // Gán OrderItem vào Order
        order.setOrderItems(Collections.singletonList(orderItem));

        // Lưu Order vào cơ sở dữ liệu
        Order savedOrder = orderRepository.save(order);
        createEnrollments(user, Collections.singletonList(orderItem));
        paymentService.createPayment(savedOrder, savedOrder.getTotalAmount(), paymentMethod.getId());
        // Xử lý thanh toán nếu phương thức thanh toán là VNPay
        if ("VNPAY".equalsIgnoreCase(paymentMethod.getCode())) {
            String paymentUrl = generateVnpayPaymentUrl(savedOrder, request);
            return OrderResponse.builder()
                    .order(savedOrder)
                    .paymentUrl(paymentUrl)
                    .build();
        }


        course.setStudentCount(course.getStudentCount() + 1);
        courseRepository.save(course);


        // Nếu không sử dụng VNPay, trả về thông tin Order
        return OrderResponse.builder()
                .order(savedOrder)
                .build();
    }

    @Override
    public Page<Order> getOrdersWithFilters(String customerName, String orderCode, String status, Date startDate, Date endDate, Pageable pageable) {
        Specification<Order> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Tìm kiếm theo tên người đặt hàng
            if (customerName != null && !customerName.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("user").get("fullName")),
                        "%" + customerName.toLowerCase() + "%"
                ));
            }

            // Tìm kiếm theo mã đơn hàng
            if (orderCode != null && !orderCode.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        root.get("id"),
                        "%" + orderCode + "%"
                ));
            }

            // Lọc theo trạng thái đơn hàng
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), Order.OrderStatus.valueOf(status)));
            }

            // Lọc theo ngày tạo (createdAt)
            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), startDate, endDate));
            }

            // Nếu chỉ có ngày bắt đầu hoặc ngày kết thúc, bạn có thể thêm các điều kiện khác như:
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return orderRepository.findAll(specification, pageable);
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

    private String generateVnpayPaymentUrl(Order order, HttpServletRequest request) {
        String vnpVersion = "2.1.0";
        String command = "pay";
        String orderType = "other";  // Sửa chính tả "orther" thành "other"
//        String vnpTxnRef = VNPayConfig.getRandomNumber(8);
        String vnpTxnRef = order.getId();
        String vnpIpAddr = VNPayConfig.getIpAddress(request);
        String locale = "vn";
        String currCode = "VND";

        // Khởi tạo các tham số cần thiết
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", command);
        vnpParams.put("vnp_TmnCode", VNPayConfig.getVnpTmnCode());

        BigDecimal totalAmount = order.getTotalAmount()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.DOWN);

        vnpParams.put("vnp_Amount", totalAmount.toString());
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_BankCode", "NCB");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang");
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_ReturnUrl", VNPayConfig.getVnpReturnUrl());
        vnpParams.put("vnp_IpAddr", vnpIpAddr);
        vnpParams.put("vnp_Locale", locale);
        vnpParams.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // Tạo chuỗi query và hash data
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                // Build hash data
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                // Build query string
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        // Tạo vnp_SecureHash sử dụng HMAC-SHA512
        String secureHash = VNPayConfig.hmacSHA512(VNPayConfig.getSecretKey(), hashData.toString());

        // Thêm vnp_SecureHash vào query string
        query.append("&vnp_SecureHash=").append(secureHash);

        System.out.println(VNPayConfig.getSecretKey());
        System.out.println(secureHash);
        System.out.println(query);

        return VNPayConfig.getVnpPayUrl() + "?" + query.toString();
    }

    private void createEnrollmentsWithActiveStatus(User user, List<OrderItem> orderItems) {
        orderItems.forEach(orderItem -> {
            Enrollment enrollment = Enrollment.builder()
                    .user(user)
                    .course(orderItem.getCourse())
                    .orderItem(orderItem)
                    .status(Enrollment.Status.ACTIVE) // Trạng thái ACTIVE
                    .progress(BigDecimal.ZERO)
                    .enrollmentDate(new Date())
                    .build();
            enrollmentRepository.save(enrollment);
        });
    }

    private Payment createPayment(Order order, PaymentMethod paymentMethod) {
        return Payment.builder()
                .order(order)
                .paymentMethod(paymentMethod)
                .amount(order.getTotalAmount())
                .status(Payment.PaymentStatus.PENDING) // Mặc định là PENDING
                .createdAt(new Date())
                .build();
    }
    






}
