package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.OrderCheckOutRequest;
import doan.ptit.programmingtrainingcenter.dto.request.OrderRequest;
import doan.ptit.programmingtrainingcenter.dto.response.OrderResponse;
import doan.ptit.programmingtrainingcenter.entity.Order;
import doan.ptit.programmingtrainingcenter.service.JwtService;
import doan.ptit.programmingtrainingcenter.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    Order addOrder(@RequestBody OrderRequest orderRequest) {
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

}
