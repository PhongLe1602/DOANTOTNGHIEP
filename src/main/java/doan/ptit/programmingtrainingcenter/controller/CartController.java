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
        return cartService.getCart(currentUser.getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping
    public SimpleResponse deleteCartByUserId() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        cartService.deleteCart(currentUser.getId());
        return SimpleResponse.success("Xóa thành công giỏ hàng");
    }
    @DeleteMapping("/item/{id}")
    public SimpleResponse deleteCartItemByUserId(@PathVariable String id) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        cartService.deleteCartItem(currentUser.getId(),id);
        return SimpleResponse.success("Xóa thành công khóa học khỏi giỏ hàng");
    }
}
