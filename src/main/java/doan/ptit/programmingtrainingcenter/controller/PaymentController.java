package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.response.PagedResponse;
import doan.ptit.programmingtrainingcenter.entity.Payment;
import doan.ptit.programmingtrainingcenter.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;


    @GetMapping
    public List<Payment> getPayments() {
        return paymentService.getAllPayments();
    }

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

    @PostMapping("/verify")
    public ResponseEntity<?> verifyVNPayPayment(@RequestBody Map<String, String> params) {
        try {
            boolean isSuccess = paymentService.verifyVNPayPayment(params);
            return ResponseEntity.ok(Map.of("success", isSuccess));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying payment.");
        }
    }

    @GetMapping("/all")
    public PagedResponse<Payment> getPayments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> paymentsPage = paymentService.getPaymentsWithFilters(status, orderId, fromDate, toDate, pageable);

        // Bao bọc dữ liệu trong PagedResponse
        return new PagedResponse<>(paymentsPage);
    }

}
