package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.PaymentMethodRequest;
import doan.ptit.programmingtrainingcenter.entity.PaymentMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentMethodService {
    PaymentMethod addPaymentMethod(PaymentMethodRequest paymentMethodRequest);
    PaymentMethod getPaymentMethod(String paymentMethodId);
    List<PaymentMethod> getAllPaymentMethods();
}
