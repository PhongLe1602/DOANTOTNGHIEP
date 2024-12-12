package doan.ptit.programmingtrainingcenter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull(message = "User id is required")
    private String userId;

    @NotNull(message = "Payment method is required")
    private String paymentMethodId;

    @NotEmpty(message = "Course list cannot be empty")
    private List<OrderItemRequest> items;

    private String notes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull(message = "Course is required")
        private String courseId;

        @NotNull(message = "Price is required")
        private BigDecimal price;
    }
}