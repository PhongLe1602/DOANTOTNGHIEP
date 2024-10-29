package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.PaymentMethodRequest;
import doan.ptit.programmingtrainingcenter.entity.PaymentMethod;
import doan.ptit.programmingtrainingcenter.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment-method")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping
    PaymentMethod addPaymentMethod(@RequestBody PaymentMethodRequest paymentMethodRequest) {
        return paymentMethodService.addPaymentMethod(paymentMethodRequest);
    }
}
