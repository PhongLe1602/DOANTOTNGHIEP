package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.PaymentMethodRequest;
import doan.ptit.programmingtrainingcenter.entity.PaymentMethod;
import org.springframework.stereotype.Service;

@Service
public interface PaymentMethodService {
    PaymentMethod addPaymentMethod(PaymentMethodRequest paymentMethodRequest);
}
