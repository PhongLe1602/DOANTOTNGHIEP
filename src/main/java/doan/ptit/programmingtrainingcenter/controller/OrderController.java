package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.OrderRequest;
import doan.ptit.programmingtrainingcenter.entity.Order;
import doan.ptit.programmingtrainingcenter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    Order addOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.addOrder(orderRequest);
    }
    @GetMapping
    List<Order> getOrders() {
        return orderService.getOrders();
    }
    @GetMapping("/{id}")
    Order getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id);
    }
}
