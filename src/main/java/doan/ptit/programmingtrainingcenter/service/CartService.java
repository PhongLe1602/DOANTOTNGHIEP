package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.CartRequest;
import doan.ptit.programmingtrainingcenter.entity.Cart;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CartService {
    Cart addCourseToCart(String userId , CartRequest cartRequest);
    Optional<Cart> getCart(String  userId);
    void deleteCart(String userId);
    void deleteCartItem(String userId, String itemId);
}
