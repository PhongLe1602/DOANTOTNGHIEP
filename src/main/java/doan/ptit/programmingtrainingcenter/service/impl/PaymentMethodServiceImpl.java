package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.PaymentMethodRequest;
import doan.ptit.programmingtrainingcenter.entity.PaymentMethod;
import doan.ptit.programmingtrainingcenter.mapper.PaymentMethodMapper;
import doan.ptit.programmingtrainingcenter.repository.PaymentMethodRepository;
import doan.ptit.programmingtrainingcenter.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;


    @Override
    public PaymentMethod addPaymentMethod(PaymentMethodRequest paymentMethodRequest) {
        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(paymentMethodRequest);

        return paymentMethodRepository.save(paymentMethod);
    }
}
