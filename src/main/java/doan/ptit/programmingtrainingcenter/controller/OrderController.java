package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.OrderCheckOutNowRequest;
import doan.ptit.programmingtrainingcenter.dto.request.OrderCheckOutRequest;
import doan.ptit.programmingtrainingcenter.dto.request.OrderRequest;
import doan.ptit.programmingtrainingcenter.dto.response.OrderResponse;
import doan.ptit.programmingtrainingcenter.dto.response.PagedResponse;
import doan.ptit.programmingtrainingcenter.entity.Order;
import doan.ptit.programmingtrainingcenter.service.JwtService;
import doan.ptit.programmingtrainingcenter.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    OrderResponse addOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.addOrder(orderRequest);
    }
    @GetMapping("/users")
    List<Order> getUserOrders(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String userId = jwtService.getUserIdFromToken(token);
        return orderService.getOrdersByUserId(userId);
    }
    @GetMapping
    List<Order> getOrders() {
        return orderService.getOrders();
    }
    @GetMapping("/{id}")
    Order getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id);
    }
    @PostMapping("/check-out")
    OrderResponse checkOut(@RequestBody OrderCheckOutRequest orderCheckOutRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String userId = jwtService.getUserIdFromToken(token);
        System.out.println(userId);
        return orderService.checkout(userId,orderCheckOutRequest);
    }
    @PostMapping("/check-now")
    OrderResponse checkoutNow(@RequestBody OrderCheckOutNowRequest orderCheckOutNowRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String userId = jwtService.getUserIdFromToken(token);
        return orderService.checkoutNow(userId,orderCheckOutNowRequest);
    }

    @GetMapping("/all")
    public ResponseEntity<PagedResponse<Order>> getOrders(
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "orderCode", required = false) String orderCode,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getOrdersWithFilters(customerName, orderCode, status, startDate, endDate, pageable);
        PagedResponse<Order> response = new PagedResponse<>(orders);
        return ResponseEntity.ok(response);
    }


}
