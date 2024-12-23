package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.OrderCheckOutNowRequest;
import doan.ptit.programmingtrainingcenter.dto.request.OrderCheckOutRequest;
import doan.ptit.programmingtrainingcenter.dto.request.OrderRequest;
import doan.ptit.programmingtrainingcenter.dto.response.OrderResponse;
import doan.ptit.programmingtrainingcenter.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponse addOrder(OrderRequest orderRequest);
    List<Order> getOrders();
    Order getOrderById(String id);
    OrderResponse checkout(String userId, OrderCheckOutRequest orderCheckOutRequest);
    List<Order> getOrdersByUserId(String userId);
    OrderResponse checkoutNow(String userId ,OrderCheckOutNowRequest orderCheckOutNowRequest);
}
