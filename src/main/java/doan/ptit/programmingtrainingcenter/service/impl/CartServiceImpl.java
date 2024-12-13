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
    public Cart addCourseToCart(String userId , CartRequest cartRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(cartRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(user, BigDecimal.ZERO));


        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getCourse().getId().equals(cartRequest.getCourseId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            throw new RuntimeException("Course already exists in the cart");
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


    @Override
    public void deleteCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAll(cart.getCartItems());
        cartRepository.delete(cart);
    }

    @Override
    public void deleteCartItem(String userId, String itemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository.findByCart_IdAndCourse_Id(cart.getId(), itemId);

        if (cartItem == null) {
            throw new RuntimeException("Cart item not found");
        }
        cartItemRepository.delete(cartItem);

    }


}
