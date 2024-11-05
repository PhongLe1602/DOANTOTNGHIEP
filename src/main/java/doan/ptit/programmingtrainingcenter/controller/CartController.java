package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.CartRequest;
import doan.ptit.programmingtrainingcenter.entity.Cart;
import doan.ptit.programmingtrainingcenter.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    Cart addCoursesCart(@RequestBody CartRequest cartRequest) {
        return cartService.addCourseToCart(cartRequest);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable String userId) {
        return cartService.getCart(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
