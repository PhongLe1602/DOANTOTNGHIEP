package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.entity.Payment;
import doan.ptit.programmingtrainingcenter.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable String id) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable String paymentId,
            @RequestParam boolean isSuccess) {

        Payment payment = paymentService.confirmPayment(paymentId, isSuccess);
        return ResponseEntity.ok(payment);
    }
}
