package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.CartRequest;
import doan.ptit.programmingtrainingcenter.entity.Cart;
import doan.ptit.programmingtrainingcenter.entity.CartItem;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.CartItemRepository;
import doan.ptit.programmingtrainingcenter.repository.CartRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Cart addCourseToCart(CartRequest cartRequest) {
        User user = userRepository.findById(cartRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(cartRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Cart cart = cartRepository.findByUserId(cartRequest.getUserId())
                .orElse(new Cart(user, BigDecimal.ZERO));


        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getCourse().getId().equals(cartRequest.getCourseId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Nếu đã tồn tại, tăng số lượng
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartRequest.getQuantity());
            cartItem.setPrice(course.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        } else {
            // Nếu chưa tồn tại, tạo mới CartItem
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .course(course)
                    .quantity(cartRequest.getQuantity())
                    .price(course.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity())))
                    .build();
            cart.addCartItem(newCartItem);
        }
        BigDecimal totalAmount = cart.getCartItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);

        // Lưu giỏ hàng
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> getCart(String userId) {
        return cartRepository.findByUserId(userId);
    }
}
