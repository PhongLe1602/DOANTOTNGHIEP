package doan.ptit.programmingtrainingcenter.dto.response;

import doan.ptit.programmingtrainingcenter.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Order order;
    private String paymentMethodCode;
    private String paymentUrl;
}
