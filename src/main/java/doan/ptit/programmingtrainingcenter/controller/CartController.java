package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.CartRequest;
import doan.ptit.programmingtrainingcenter.dto.response.SimpleResponse;
import doan.ptit.programmingtrainingcenter.entity.Cart;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    Cart addCoursesCart(@RequestBody CartRequest cartRequest) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return cartService.addCourseToCart(currentUser.getId(),cartRequest);
    }
    @GetMapping
    public ResponseEntity<?> getCartByUserId() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Optional<Cart> cart = cartService.getCart(currentUser.getId());

        return cart.<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(new Cart()));

    }
    @DeleteMapping
    public SimpleResponse deleteCartByUserId() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        cartService.deleteCart(currentUser.getId());
        return SimpleResponse.success("Xóa thành công giỏ hàng");
    }
    @DeleteMapping("/item/{cartItemId}")
    public SimpleResponse deleteCartItemByUserId(@PathVariable String cartItemId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        cartService.deleteCartItem(currentUser.getId(),cartItemId);
        return SimpleResponse.success("Xóa thành công khóa học khỏi giỏ hàng");
    }
}
