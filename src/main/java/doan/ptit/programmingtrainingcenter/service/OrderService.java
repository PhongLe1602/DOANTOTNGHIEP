package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.OrderRequest;
import doan.ptit.programmingtrainingcenter.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Order addOrder(OrderRequest orderRequest);
    List<Order> getOrders();
    Order getOrderById(String id);
}
