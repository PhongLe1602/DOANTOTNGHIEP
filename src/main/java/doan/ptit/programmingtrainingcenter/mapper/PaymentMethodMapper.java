package doan.ptit.programmingtrainingcenter.mapper;

import doan.ptit.programmingtrainingcenter.dto.request.PaymentMethodRequest;
import doan.ptit.programmingtrainingcenter.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    @Mapping(target = "id", ignore = true) // Bỏ qua ID khi tạo mới
    @Mapping(target = "createdAt", ignore = true) // Bỏ qua thời gian tạo
    @Mapping(target = "updatedAt", ignore = true) // Bỏ qua thời gian cập nhật
    PaymentMethod toEntity(PaymentMethodRequest paymentMethodRequest);

    @Mapping(target = "createdAt", ignore = true) // Bỏ qua thời gian tạo khi cập nhật
    void updatePaymentMethod(@MappingTarget PaymentMethod paymentMethod, PaymentMethodRequest paymentMethodRequest);
}
